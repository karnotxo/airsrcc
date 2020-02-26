package com.indra.srcc.airsrcc.units;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Frequency;
import javax.measure.quantity.Length;
import javax.measure.quantity.Power;
import javax.measure.quantity.Speed;

import systems.uom.common.USCustomary;

import javax.measure.MetricPrefix;

import tech.units.indriya.AbstractSystemOfUnits;
import tech.units.indriya.AbstractUnit;
import tech.units.indriya.format.LocalUnitFormat;
import tech.units.indriya.format.SymbolMap;
import tech.units.indriya.function.LogConverter;
import tech.units.indriya.function.MultiplyConverter;
import tech.units.indriya.unit.BaseUnit;
import tech.units.indriya.unit.TransformedUnit;
import tech.units.indriya.unit.Units;

public class DerivedUnits extends AbstractSystemOfUnits {

	private static Map<Locale, LocalUnitFormat> unitFormats = new HashMap<Locale, LocalUnitFormat>();
	private static Map<Locale, LocalUnitFormat> defaultUnitFormats = new HashMap<Locale, LocalUnitFormat>();
	private static Map<Locale, SymbolMap> symbolMaps = new HashMap<Locale, SymbolMap>();

	/**
	 * The singleton instance.
	 */
	private static final DerivedUnits INSTANCE = new DerivedUnits();

	/**
	 * DefaultQuantityFactory constructor (prevents this class from being
	 * instantiated).
	 */
	private DerivedUnits() {
	}

	protected static LocalUnitFormat getUnitFormat() {
		return getUnitFormat(Locale.getDefault());
	}

	protected static LocalUnitFormat getUnitFormat(Locale locale) {
		if (!unitFormats.containsKey(locale)) {
			try {
				symbolMaps.put(locale, SymbolMap.of(ResourceBundle.getBundle("i18n.format.messages", locale)));
			} catch (java.util.MissingResourceException e) {
				symbolMaps.put(locale, SymbolMap.of(ResourceBundle.getBundle("i18n.format.messages")));
			}
			unitFormats.put(locale, LocalUnitFormat.getInstance(symbolMaps.get(locale)));
		}
		return unitFormats.get(locale);
	}

	protected static LocalUnitFormat getDefaultUnitFormat() {
		return getDefaultUnitFormat(Locale.getDefault());
	}

	protected static LocalUnitFormat getDefaultUnitFormat(Locale locale) {
		if (!defaultUnitFormats.containsKey(locale)) {
			try {
				defaultUnitFormats.put(locale, LocalUnitFormat.getInstance(locale));
			} catch (java.util.MissingResourceException e) {
				defaultUnitFormats.put(locale, LocalUnitFormat.getInstance());
			}

		}
		return defaultUnitFormats.get(locale);
	}

	public static Appendable format(Unit<?> unit, Appendable appendable) throws IOException {
		if (getUnitFormat(Locale.getDefault()) != null && symbolMaps.get(Locale.getDefault()).getSymbol(unit) != null)
			getUnitFormat().format(unit, appendable);
		else
			getDefaultUnitFormat().format(unit, appendable);
		return appendable;
	}

	/**
	 * Returns the singleton instance of this class.
	 *
	 * @return the metric system instance.
	 */
	public static DerivedUnits getInstance() {
		return INSTANCE;
	}

	public static final Unit<Power> KILOWATT = addUnit(
			new TransformedUnit<Power>("kW", Units.WATT, MultiplyConverter.ofPrefix(MetricPrefix.KILO)), Power.class);

	public static final Unit<Power> MEGAWATT = addUnit(
			new TransformedUnit<Power>("MW", Units.WATT, MultiplyConverter.ofPrefix(MetricPrefix.MEGA)), Power.class);

	public static final Unit<Power> DECIBELMILLIWATT = addUnit(
			new TransformedUnit<Power>("dBm", Units.WATT,
					MultiplyConverter.ofPrefix(MetricPrefix.MILLI).inverse()
							.concatenate(new LogConverter(10).inverse().concatenate(MultiplyConverter.ofRational(1, 10)))),
			Power.class);

	/*
	 * public static final BaseUnit<Dimensionless> VOLT_AMPERE = addUnit( new
	 * BaseUnit<Dimensionless>("VA", QuantityDimension.NONE),
	 * Dimensionless.class);
	 */

	public static final BaseUnit<ApparentPower> VOLT_AMPERE = addUnit(new BaseUnit<ApparentPower>("VA"),
			ApparentPower.class); // Dimension equals WATT

	/*
	 * public static final BaseUnit<Dimensionless> VOLT_AMPERE_REACTIVE =
	 * addUnit( new BaseUnit<Dimensionless>("VAR", QuantityDimension.NONE),
	 * Dimensionless.class);
	 */
	public static final BaseUnit<ReactivePower> VOLT_AMPERE_REACTIVE = addUnit(new BaseUnit<ReactivePower>("var"),
			ReactivePower.class);// Dimension equals WATT
	/*
	 * The correct symbol is var and not Var, VAr, or VAR
	 * https://en.wikipedia.org/wiki/Volt-ampere_reactive
	 */

	public static final Unit<ApparentPower> KVA = addUnit(
			new TransformedUnit<ApparentPower>("kVA", VOLT_AMPERE, MultiplyConverter.ofPrefix(MetricPrefix.KILO)), ApparentPower.class);

	public static final Unit<ReactivePower> KVAR = addUnit(
			new TransformedUnit<ReactivePower>("kvar", VOLT_AMPERE_REACTIVE, MultiplyConverter.ofPrefix(MetricPrefix.KILO)),
			ReactivePower.class);

	/***************************************************************************/
	public static final Unit<Dimensionless> DECIBEL = addUnit(
			AbstractUnit.ONE.transform(new LogConverter(10).inverse().concatenate(MultiplyConverter.ofRational(1, 10))));

	/*
	 * De esta forma esta implementado en la libreria UOM
	 * [tec.units.ri.spi.internal.NonSI.DECIBEL]pero al cambiar el xml y darle
	 * la implementacion no aparecen las unidades.
	 * 
	 * 
	 * static final Unit<Dimensionless> DECIBEL = addUnit(AbstractUnit.ONE
	 * .transform(new LogConverter(10).inverse().concatenate( new
	 * RationalConverter(1d, 10d))));
	 */

	public static final Unit<Frequency> KILOHERTZ = addUnit(
			new TransformedUnit<Frequency>("kHz", Units.HERTZ, MultiplyConverter.ofPrefix(MetricPrefix.KILO)), Frequency.class);

	public static final Unit<Frequency> MEGAHERTZ = addUnit(
			new TransformedUnit<Frequency>("MHz", Units.HERTZ, MultiplyConverter.ofPrefix(MetricPrefix.MEGA)), Frequency.class);

	public static final Unit<Frequency> GIGAHERTZ = addUnit(
			new TransformedUnit<Frequency>("GHz", Units.HERTZ, MultiplyConverter.ofPrefix(MetricPrefix.GIGA)), Frequency.class);

	// public static final Unit<AngularSpeed> REVOLUTIONS_PER_MINUTE =
	// addUnit(US.REVOLUTION.divide(US.MINUTE).asType(AngularSpeed.class));
	// Da ERROR: tec.uom.se.quantity.QuantityDimension getInstance
	// ADVERTENCIA: Quantity type: interface javax.measure.quantity.AngularSpeed
	// unknown
	public static final Unit<Frequency> REVOLUTIONS_PER_MINUTE = addUnit(
			new TransformedUnit<Frequency>("rpm", Units.HERTZ, MultiplyConverter.ofRational(1, 60)), Frequency.class);

	// public static final Unit<Dimensionless> PERCENT =
	// addUnit(AbstractUnit.ONE.divide(100.0D));
	// already in
	// tec.uom.se.spi.PERCENT

	public static final Unit<Length> FL = addUnit(USCustomary.FOOT).multiply(100);
	
	public static final Unit<Length> MILE = Units.METRE.multiply(1609.344).asType(Length.class);
	

	public static final Unit<Speed> NAUTICAL_MILE_PER_SECOND = addUnit(USCustomary.NAUTICAL_MILE.divide(Units.SECOND))
			.asType(Speed.class);

	@Override
	public String getName() {
		return "SI-EXT";
	}

	/**
	 * Adds a new unit not mapped to any specified quantity type.
	 *
	 * @param unit
	 *            the unit being added.
	 * @return <code>unit</code>.
	 */
	private static <U extends Unit<?>> U addUnit(U unit) {
		INSTANCE.units.add(unit);
		return unit;
	}

	/**
	 * Adds a new unit and maps it to the specified quantity type.
	 *
	 * @param unit
	 *            the unit being added.
	 * @param type
	 *            the quantity type.
	 * @return <code>unit</code>.
	 */
	private static <U extends AbstractUnit<?>> U addUnit(U unit, Class<? extends Quantity<?>> type) {
		INSTANCE.units.add(unit);
		INSTANCE.quantityToUnit.put(type, unit);
		return unit;
	}

}
