/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dashfx.lib.data;

/**
 *
 * @author patrick
 */
public enum SmartValueTypes
{
	Integer(1),
	Double(2),
	Float(2),
	Number(3),
	String(4),
	Hash(8),
	Grouped(0x10),
	GroupedHash(0x18),
	Array(0x20),
	IntegerArray(0x21),
	DoubleArray(0x22),
	FloatArray(0x22),
	NumberArray(0x23),
	StringArray(0x24),
	ObjectArray(0x28),
	Boolean(0x40),
	BooleanArray(0x60),
	Other(0x8000000),
	Unknown(0xFFFFFFFF);
	private final int mask;

	SmartValueTypes(int mask)
	{
		this.mask = mask;
	}

	public int getMask()
	{
		return mask;
	}

	public boolean isArray()
	{
		return (this.mask & Array.mask) != 0;
	}
}
