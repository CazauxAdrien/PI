package com.imrglobal.framework.utils;


/**
 * This class allows to format a string according to :
 * <ul>
 * <li>A type of justification : LEFT, RIGHT or CENTER
 * <li>A minimum size
 * <li>A maximum size
 * </ul>
 * If the original string is smaller than the minimum size, the string is completed
 * using a specified character (by default, we use a blank). According to the type of justification, the characters
 * are addes at the end, the start or the end and the start of the string.
 * <p>
 * The format used can be specified using a string. This string at the folowing format :
 * <br>
 * <code>[R|L|C justification code][min length].[max length]'[charatcer used to complete]''[char used to complete at rigth when justification is center]'</code>
 * <p>
 * Below are various format modifier examples.
 * <p>
 * <TABLE BORDER=1 CELLPADDING=8>
 *   <th>Format modifier
 *   <th>Justify
 *   <th>minimum width
 *   <th>maximum width
 *   <th>comment
 *
 *   <tr>
 *   <td align=center>20</td>
 *   <td align=center>Left</td>
 *   <td align=center>20</td>
 *   <td align=center>none</td>
 *   <td>Right pad with spaces if the string is less than 20
 *   characters long.</td>
 *	</tr>
 *
 *   <tr>
 *	<td align=center>R20</td>
 *	<td align=center>Right</td>
 *	<td align=center>20</td>
 *	<td align=center>none</td>
 *	<td>Left pad with spaces if the string is less than 20 characters long.</td>
 *	</tr>
 *   <tr>
 *	<td align=center>C20</td>
 *	<td align=center>Center</td>
 *	<td align=center>20</td>
 *	<td align=center>none</td>
 *	<td>Left and right pad with spaces if the string is less than 20 characters long.</td>
 *	</tr>
 *   <tr>
 *   <td align=center>.30</td>
 *   <td align=center>NA</td>
 *   <td align=center>none</td>
 *   <td align=center>30</td>
 *   <td>Truncate from the beginning if the string is longer than 30
 *   characters.</td>
 *	</tr>
 *   <tr>
 *   <td align=center>20.30</td>
 *   <td align=center>Left</td>
 *   <td align=center>20</td>
 *   <td align=center>30</td>
 *   <td>Right pad with spaces if the string is shorter than 20
 *   characters. However, if the string is longer than 30 characters,
 *   then truncate from the beginning.</td>
 *	</tr>
 *   <tr>
 *   <td align=center>R20.30'>'</td>
 *   <td align=center>Right</td>
 *   <td align=center>20</td>
 *   <td align=center>30</td>
 *   <td>Left pad with '>' if the string is shorter than 20
 *   characters. However, if the string is longer than 30 characters,
 *   then truncate from the beginning.</td>
 *	</tr>
 *   <tr>
 *   <td align=center>C20.30'<''>'</td>
 *   <td align=center>Center</td>
 *   <td align=center>20</td>
 *   <td align=center>30</td>
 *   <td>Left pad with '<'  and right pad with '>' if the string is shorter than 20
 *   characters. However, if the string is longer than 30 characters,
 *   then truncate from the beginning.</td>
 *	</tr>
 *   </table>
 */
public class StringFormatter {

	public static final int LEFT_JUSTIFICATION = 0;
	public static final int RIGHT_JUSTIFICATION = 2;
	public static final int CENTER_JUSTIFICATION = 1;

	private int justification;
	private int minLength;
	private int maxLength;
	private char charToJustify = ' ';
	private char charToJustifyAtRight = ' ';

	public StringFormatter() {
		this.justification = LEFT_JUSTIFICATION;
		this.minLength = -1;
		this.maxLength = Integer.MAX_VALUE;
		this.charToJustify = ' ';
		this.charToJustifyAtRight = ' ';
	}

	public StringFormatter(String format) {
		this();
		if (format != null) {
			parse(format);
		}
	}


	private void parse(String format) {
		char c;
		int i = 0;
		boolean isInInt = false;
		boolean justificationDone = false;
		boolean minLengthDone = false;
		boolean maxLengthDone = false;
		boolean charToJustifyDone = false;
		boolean charToJustify2Done = false;
		StringBuilder intString = new StringBuilder();
		while (i < format.length()) {
			c = format.charAt(i++);
			if (c == '\'') {
				if (isInInt) {
					int length = Integer.parseInt(intString.toString());
					if (!minLengthDone) {
						this.minLength = length;
					} else if (!maxLengthDone) {
						this.maxLength = length;
					}
				}
				minLengthDone = true;
				maxLengthDone = true;
				justificationDone = true;
				c = format.charAt(i++);
				if (!charToJustifyDone) {
					this.charToJustify = c;
					charToJustifyDone = true;
				} else {
					charToJustifyAtRight = c;
					charToJustify2Done = true;
				}
				c = format.charAt(i++);
				if (c != '\'') {
					while (c != '\'') {
						c = format.charAt(i++);
					}
				}
			} else if (c == '.') {
				if (isInInt) {
					this.minLength = Integer.parseInt(intString.toString());
					isInInt = false;
				}
				minLengthDone = true;
				justificationDone = true;
			} else if (Character.isDigit(c)) {
				if (!isInInt) {
					isInInt = true;
					intString = new StringBuilder();
					intString.append(c);
				} else {
					intString.append(c);
				}
			} else {
				if (!justificationDone) {
					if ("RCL".indexOf(c) >= 0) {
						switch (c) {
							case 'L':
								this.justification = LEFT_JUSTIFICATION;
								break;
							case 'R':
								this.justification = RIGHT_JUSTIFICATION;
								break;
							case 'C':
								this.justification = CENTER_JUSTIFICATION;
								break;
						}
						justificationDone = true;
					} else {
						System.err.println("Invalid character [" + c + "] at position [" + (i - 1) + "] in String format [" + format + "]");
					}
				} else {
					System.err.println("Unexpected character [" + c + "] at position [" + (i - 1) + "] in String format [" + format + "]");
				}
			}
		}
		if (isInInt) {
			int length = Integer.parseInt(intString.toString());
			if (!minLengthDone) {
				this.minLength = length;
			} else if (!maxLengthDone) {
				this.maxLength = length;
			}
		}
		if (!charToJustify2Done) {
			this.charToJustifyAtRight = this.charToJustify;
		}
		if (this.maxLength < this.minLength) {
			System.err.println("Error max length [" + this.maxLength + "] < min lenth [" + this.minLength + "] in String format [" + format + "]!!!");
			this.maxLength = this.minLength;
		}
	}

	/**
	 * Returns a new String according to the given format.
	 */
	public static String format(String format, String str) {
		String res = str;
		if (format != null) {
			res = (new StringFormatter(format)).format(str);
		}
		return (res);
	}

	/**
	 * Format the given String.
	 */
	public String format(String s) {

		String res = s;
		if (s != null) {
			int l = s.length();
			if (l > this.maxLength) {
				res = s.substring(0, this.maxLength);
			}
			if (l < this.maxLength) {
				res = complete(s);
			}
		} else {
			res = complete(s);
		}
		return (res);
	}

	private String complete(String s) {
    StringBuilder buf = new StringBuilder();
		int l = (s == null) ? 0 : s.length();
		if (this.justification == CENTER_JUSTIFICATION) {
			int n = (this.minLength - l) / 2;
			for (int i = 0; i < n; i++) {
				buf.append(charToJustify);
			}
			buf.append(s);
			for (int i = 0; i < ((this.minLength - l) - n); i++) {
				buf.append(charToJustifyAtRight);
			}
		} else if (this.justification == RIGHT_JUSTIFICATION) {
			for (int i = 0; i < (this.minLength - l); i++) {
				buf.append(charToJustify);
			}
			buf.append(s);
		} else {
			buf.append(s);
			for (int i = 0; i < (this.minLength - l); i++) {
				buf.append(charToJustify);
			}
		}
		return (buf.toString());
	}


	public char getCharToJustify() {
		return this.charToJustify;
	}

	public void setCharToJustify(char charToJustify) {
		this.charToJustify = charToJustify;
	}


	public char getCharToJustifyAtRight() {
		return this.charToJustifyAtRight;
	}

	public void setCharToJustifyAtRight(char charToJustifyAtRight) {
		this.charToJustifyAtRight = charToJustifyAtRight;
	}


	public int getJustification() {
		return this.justification;
	}

	public void setJustification(int justification) {
		this.justification = justification;
	}


	public int getMaxLength() {
		return this.maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}


	public int getMinLength() {
		return this.minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public String getFormat() {
    StringBuilder buf = new StringBuilder();
		switch (this.justification) {
			case LEFT_JUSTIFICATION:
				buf.append('L');
				break;
			case RIGHT_JUSTIFICATION:
				buf.append('R');
				break;
			case CENTER_JUSTIFICATION:
				buf.append('C');
				break;
			default:
				break;
		}
		if (this.minLength > 0) {
			buf.append(this.minLength);
		}
		if (this.maxLength != Integer.MAX_VALUE) {
			buf.append('.');
			buf.append(this.maxLength);
		}
		if (this.charToJustify != this.charToJustifyAtRight) {
			buf.append('\'').append(this.charToJustify).append('\'');
			buf.append('\'').append(this.charToJustifyAtRight).append('\'');
		} else {
			if (this.charToJustify != ' ') {
				buf.append('\'').append(this.charToJustify).append('\'');
			}
		}
		return (buf.toString());
	}

	public String toString() {
    StringBuilder buf = new StringBuilder();
		buf.append("String formatter using format [").append(getFormat()).append("]");
		return (buf.toString());
	}
}