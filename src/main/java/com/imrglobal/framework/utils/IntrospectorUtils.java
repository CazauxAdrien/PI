package com.imrglobal.framework.utils;

import java.awt.Image;
import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utilitaire d'encapsulation des services d'introspection
 * <br>Complete le BeanInfo d'une classe en analysant les interfaces.
 * @see Introspector
 */
public class IntrospectorUtils {

  private static final Map<Class,BeanInfo> BEAN_INFO_CACHE = new ConcurrentHashMap<>();

  /**
   * Analyse un Java bean et indique ces propriétés, méthodes exposées en complétant l'analyse avec l'analyse de toutes les interfaces définies par la classes et ces classes mères.
   * <p>
   * Complète l'analyse de {@link Introspector#getBeanInfo(Class, Class)} en ajoutant les méthodes et les propriétés définies par les interfaces de la classe et des ces
   * classes parentes.
   * <p>
   * On ne prend en compte que les propriétés définies par les interfaces dont l'accesseur en lecture est de type default s'il existe, ou si l'accesseur en écriture est de type default si l'accesseur en lecture n'existe pas.
   * <p>
   * Pour les méthodes d'interfaces on ne prend en compte que les méthodes static ou default.
   * @param stopClass La classe mère à partir de laquelle on stoppe l'analyse. Aucune propriétés / méthodes / interafces de cette classe ou de ces classes parents ne seront prise en compte.
   * @return le beanInfo de la classe
   * @throws IntrospectionException
   */
  public static BeanInfo getBeanInfo(Class cls) throws IntrospectionException {
    BeanInfo beanInfo = BEAN_INFO_CACHE.get(cls);
    if (beanInfo == null) {
      beanInfo = initBeanInfo(cls);
    }
    return beanInfo;
  }

  /**
   * Analyse un Java bean et indique ces propriétés, méthodes exposées, jusqu'à une 'stopClass' spécifiée.
   * <p>
   * Complète l'analyse de {@link Introspector#getBeanInfo(Class, Class)} en ajoutant les méthodes et les propriétés définies par les interfaces de la classe et des ces
   * classes parentes jusqu'à la stopClass spécifiée.
   * <p>
   * On ne prend en compte que les propriétés définies par les interfaces dont l'accesseur en lecture est de type default s'il existe, ou si l'accesseur en écriture est de type default si l'accesseur en lecture n'existe pas.
   * <p>
   * Pour les méthodes d'interfaces on ne prend en compte que les méthodes static ou default.
   * <p>
   * Remarque : l'analyse des interfaces se limite aux interfaces déclarées pas les classes jusqu'à la 'stopClass' spécifiée. On ne prend donc en compte que les éventuelles propriétés définies par ces interfaces.
   * <p>Cela peut conduire à des résultats qui peuvent sembler étonnant. Par exemple :
   * <ul>
   * <li>la classe {@code MonConceptA} hérite de {@code AbstractMonConceptA} qui implémente {@code IConceptA}  qui déclare une méthode default {@code getMyPropA()}</li>
   * <li>la classe {@code MonConceptB} hérite de {@code MonConceptA} et implémente une interface {@code IConceptB} qui elle-même hérite de {@code IConceptA}</li>
   * </ul>
   * {@code IntrospectorsUtils.getBeanInfo(MonConceptA.class, AbstractConceptA.class)} ne retourne pas la propriété {@code myPropA} car l’interface IConceptA n’est pas analysée (on s’arrête à AbstractConceptA).
   * <p>Par contre : {@code IntrospectorsUtils.getBeanInfo(MonConceptB.class, AbstractConceptA.class)} retourne la propriété {@code myPropA} car il la trouve via l’analyse de l’interface IConceptB et donc de ces interfaces parentes.
   * @param stopClass La classe mère à partir de laquelle on stoppe l'analyse. Aucune propriétés / méthodes / interafces de cette classe ou de ces classes parents ne seront prise en compte.
   * @return le beanInfo de la classe
   * @throws IntrospectionException
   */
  public static BeanInfo getBeanInfo(Class cls, Class stopClass) throws IntrospectionException {
    if (stopClass == null) {
      return getBeanInfo(cls);
    }
    return retrieveBeanInfo(cls, stopClass);
  }

  private synchronized static BeanInfo initBeanInfo(Class cls) throws IntrospectionException {
    BeanInfo beanInfo = BEAN_INFO_CACHE.get(cls);
    if (beanInfo == null) {
      beanInfo = retrieveBeanInfo(cls);
      BEAN_INFO_CACHE.put(cls, beanInfo);
    }
    return beanInfo;
  }

  private static BeanInfo retrieveBeanInfo(Class cls) throws IntrospectionException {
    BeanInfo beanInfo = Introspector.getBeanInfo(cls);
    List<Class> allInterfaces = InterfaceUtils.getAllInterfaces(cls);
    if (!allInterfaces.isEmpty()) {
      return completeWithInterfacesInfos(beanInfo, allInterfaces, Modifier.isAbstract(cls.getModifiers()));
    }
    return beanInfo;
  }

  private static BeanInfo retrieveBeanInfo(Class cls, Class stopClass) throws IntrospectionException {
    BeanInfo beanInfo = Introspector.getBeanInfo(cls, stopClass);
    List<Class> allInterfaces = InterfaceUtils.getInterfaces(cls, stopClass);
    if (!allInterfaces.isEmpty()) {
      return completeWithInterfacesInfos(beanInfo, allInterfaces, true);
    }
    return beanInfo;
  }

  /**
   * complète les infos avec le résultat de l'anaylse des interfaces spécifié
   * @param checkInterfaceProperties indique que l'on ne prend en compte que les propriétés issues des interfaces pour lesquelles l'accesseur en lecture (ou en écriture) est default. Ce contrôle n'est nécessaire que si
   *                                 on analyse avec une stopClass. Dans le cas d'une analyse complète d'une classe non abstraite, il est inutiles, les autres propriétés étant forcément implémentées au niveau de la classe ou d'une de ces classes parentes.
   * @return
   */
  private static BeanInfo completeWithInterfacesInfos(BeanInfo beanInfo, List<Class> allInterfaces, boolean checkInterfaceProperties) {
    BeanInfoInterfaceAnalyser analyser = new BeanInfoInterfaceAnalyser(beanInfo, checkInterfaceProperties);
    analyser.analyseInterfaces(allInterfaces);
    if (analyser.isInterfacesProperties() || analyser.isInterfacesMethods()) {
      return new CompleteBeanInfo(beanInfo, analyser.getInterfacesProperties(), analyser.getInterfacesMethods());
    }
    return beanInfo;
  }



  static class CompleteBeanInfo implements BeanInfo {
    BeanInfo wrappedBeanInfo;
    PropertyDescriptor[] properties;
    MethodDescriptor[] methods;

    public CompleteBeanInfo(BeanInfo wrappedBeanInfo, List<PropertyDescriptor> otherProperties, List<MethodDescriptor> otherMethods) {
      this.wrappedBeanInfo = wrappedBeanInfo;
      initProperties(wrappedBeanInfo, otherProperties);
      initMethods(wrappedBeanInfo, otherMethods);
    }

    private void initProperties(BeanInfo wrappedBeanInfo, List<PropertyDescriptor> otherProperties) {
      PropertyDescriptor[] propertyDescriptors = wrappedBeanInfo.getPropertyDescriptors();
      if (otherProperties.isEmpty()) {
        this.properties = propertyDescriptors;
      } else {
        int length = propertyDescriptors.length;
        int otherSize = otherProperties.size();
        properties = new PropertyDescriptor[length + otherSize];
        System.arraycopy(propertyDescriptors, 0, properties, 0, length);
        PropertyDescriptor[] others = otherProperties.toArray(new PropertyDescriptor[otherSize]);
        System.arraycopy(others, 0, properties, length, otherSize);
      }
    }

    private void initMethods(BeanInfo wrappedBeanInfo, List<MethodDescriptor> otherMethods) {
      MethodDescriptor[] methodDescriptors = wrappedBeanInfo.getMethodDescriptors();
      if (otherMethods.isEmpty()) {
        this.methods = methodDescriptors;
      } else {
        int length = methodDescriptors.length;
        int otherSize = otherMethods.size();
        methods = new MethodDescriptor[length + otherSize];
        System.arraycopy(methodDescriptors, 0, methods, 0, length);
        MethodDescriptor[] others = otherMethods.toArray(new MethodDescriptor[otherSize]);
        System.arraycopy(others, 0, methods, length, otherSize);
      }
    }

    @Override
    public BeanDescriptor getBeanDescriptor() {
      return wrappedBeanInfo.getBeanDescriptor();
    }

    @Override
    public EventSetDescriptor[] getEventSetDescriptors() {
      return wrappedBeanInfo.getEventSetDescriptors();
    }

    @Override
    public int getDefaultEventIndex() {
      return wrappedBeanInfo.getDefaultEventIndex();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
      return properties;
    }

    @Override
    public int getDefaultPropertyIndex() {
      return wrappedBeanInfo.getDefaultPropertyIndex();
    }

    @Override
    public MethodDescriptor[] getMethodDescriptors() {
      return methods;
    }

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
      return wrappedBeanInfo.getAdditionalBeanInfo();
    }

    @Override
    public Image getIcon(int iconKind) {
      return wrappedBeanInfo.getIcon(iconKind);
    }
  }

  static class BeanInfoInterfaceAnalyser {

    Map<String, PropertyDescriptor> initialProperties;
    Map<String, PropertyDescriptor> interfacesProperties = new HashMap<>();

    Map<String, List<MethodDescriptor>> initialMethods;
    Map<String, List<MethodDescriptor>> interfacesMethods = new HashMap<>();

    Set<Class> interfacesAnalized = new HashSet<>();

    boolean checkInterfaceProperties;

    public BeanInfoInterfaceAnalyser(BeanInfo beanInfo, boolean checkInterfaceProperties) {
      this.checkInterfaceProperties = checkInterfaceProperties;
      initialProperties = new HashMap<>();
      Arrays.asList(beanInfo.getPropertyDescriptors()).forEach(p -> initialProperties.put(p.getName(),p));
      initialMethods = new HashMap<>();
      Arrays.asList(beanInfo.getMethodDescriptors()).forEach(m->initialMethods.computeIfAbsent(m.getName(),k -> new ArrayList<>()).add(m));
    }

    protected void analyseInterfaces(List<Class> interfaces) {
      interfacesAnalized.clear();
      interfacesProperties.clear();
      interfacesMethods.clear();
      interfaces.forEach(this::analyseInterface);
    }

    boolean isInterfacesProperties() {
      return !interfacesProperties.isEmpty();
    }

    boolean isInterfacesMethods() {
      return !interfacesMethods.isEmpty();
    }

    List<PropertyDescriptor> getInterfacesProperties() {
      return new ArrayList<>(interfacesProperties.values());
    }

    List<MethodDescriptor> getInterfacesMethods() {
      List<MethodDescriptor> methods = new ArrayList<>();
      interfacesMethods.values().forEach(methods::addAll);
      return methods;
    }

    private void analyseInterface(Class anInterface) {
      if (!interfacesAnalized.contains(anInterface)) {
        interfacesAnalized.add(anInterface);
        analyseInterfaceInfo(anInterface);
        Class[] superInterfaces = anInterface.getInterfaces();
        if (superInterfaces !=null && superInterfaces.length>0) {
          Arrays.asList(superInterfaces).forEach(this::analyseInterface);
        }
      }
    }

    private void analyseInterfaceInfo(Class anInterface) {
      try {
        BeanInfo interfaceInfo = Introspector.getBeanInfo(anInterface);
        analyseInterfaceProperties(interfaceInfo.getPropertyDescriptors());
        analyseInterfaceMethods(interfaceInfo.getMethodDescriptors());
      } catch (IntrospectionException e) {
        System.err.println("IntrospectorUtils problem to analyse interface "+anInterface);
      }
    }

    private void analyseInterfaceProperties(PropertyDescriptor[] interfaceProperties) {
      Arrays.asList(interfaceProperties).forEach(this::analyseInterfaceProperty);
    }

    private void analyseInterfaceProperty(PropertyDescriptor interfaceProperty) {
      String name = interfaceProperty.getName();
      if (!initialProperties.containsKey(name) && !interfacesProperties.containsKey(name) ) {
        if (!this.checkInterfaceProperties || isValidInterfaceProperty(interfaceProperty)) {
          interfacesProperties.put(name,interfaceProperty);
        }
      }
    }

    private boolean isValidInterfaceProperty(PropertyDescriptor interfaceProperty) {
      Method readMethod = interfaceProperty.getReadMethod();
      if (readMethod != null) {
        return isValidMethodInterface(readMethod);
      }
      return isValidMethodInterface(interfaceProperty.getWriteMethod());
    }

    private void analyseInterfaceMethods(MethodDescriptor[] methods) {
      Arrays.asList(methods).forEach(this::analyseInterfaceMethod);
    }

    private void analyseInterfaceMethod(MethodDescriptor method) {
      if (isNewMethod(method)) {
        interfacesMethods.computeIfAbsent(method.getName(),k->new ArrayList<>()).add(method);
      }
    }

    boolean isNewMethod(MethodDescriptor method) {
      if (isMethodInterfacePossible(method)) {
        List<MethodDescriptor> methodsWithSameName = methodsWithSameName(method.getName());
        if (methodsWithSameName.isEmpty()) {
          return true;
        }
        return !isMethodsWithSameNameAndSameParamters(method, methodsWithSameName);
      }
      return false;
    }

    List<MethodDescriptor> methodsWithSameName(String name) {
      List<MethodDescriptor> methods = new ArrayList<>();
      List<MethodDescriptor> initalMethodsWithSameName = initialMethods.get(name);
      if (initalMethodsWithSameName != null) {
        methods.addAll(initalMethodsWithSameName);
      }
      List<MethodDescriptor> newMethodsWithSameName = initialMethods.get(name);
      if (newMethodsWithSameName != null) {
        methods.addAll(newMethodsWithSameName);
      }
      return methods;
    }

    boolean isMethodInterfacePossible(MethodDescriptor methodDescriptor) {
      Method method = methodDescriptor.getMethod();
      return isValidMethodInterface(method);
    }

    private boolean isValidMethodInterface(Method method) {
      if (method == null) {
        return false;
      }
      return method.isDefault() || Modifier.isStatic(method.getModifiers());
    }

    boolean isMethodsWithSameNameAndSameParamters(MethodDescriptor method, List<MethodDescriptor> methodsWithSameName) {
      for (MethodDescriptor methodWithSameName : methodsWithSameName) {
        if (isSameParameters(method, methodWithSameName)) {
          return true;
        }
      }
      return false;
    }

    boolean isSameParameters(MethodDescriptor method, MethodDescriptor methodWithSameName) {
      ParameterDescriptor[] params1 = method.getParameterDescriptors();
      ParameterDescriptor[] params2 = methodWithSameName.getParameterDescriptors();
      if (params1 == null && params2==null) {
        return true;
      }
      if (params1 != null && params2 != null && params1.length == params2.length) {
        for (int i =0;i<params1.length;i++) {
          ParameterDescriptor param1 = params1[i];
          ParameterDescriptor param2 = params2[i];
          if (!param1.getClass().equals(param2.getClass())) {
            return false;
          }
        }
        return true;
      }
      return false;
    }
  }


}
