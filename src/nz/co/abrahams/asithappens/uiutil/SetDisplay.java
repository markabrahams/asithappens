/*
 * SetDisplay.java
 *
 * Created on 14 June 2008, 23:37
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

package nz.co.abrahams.asithappens.uiutil;

import java.awt.Color;

/**
 *
 * @author mark
 */
public class SetDisplay {
    
    public enum Positioning { Grounded, Stacked };
    
    public enum Style { Fill, Line };
    
    public static final SetDisplay[] BANDWIDTH_DEFAULTS = {
        new SetDisplay(Style.Fill, Color.GREEN),
        new SetDisplay(Style.Line, Color.BLUE)
    };
    
    public static final SetDisplay[] RESPONSE_DEFAULTS = {
        new SetDisplay(Style.Fill, Color.GREEN)
    };
    
    public static final SetDisplay[] PROCESSOR_DEFAULTS = {
        new SetDisplay(Style.Line, Color.BLUE)
    };
    
    public static final SetDisplay[] NETSNMP_PROCESSOR_DEFAULTS = {
        new SetDisplay(Style.Fill, Color.GREEN),
        new SetDisplay(Style.Fill, Color.BLUE),
        new SetDisplay(Style.Fill, Color.RED),
        new SetDisplay(Style.Fill, Color.YELLOW),
        new SetDisplay(Style.Fill, Color.MAGENTA),
        new SetDisplay(Style.Fill, Color.CYAN)
    };
    
    public static final SetDisplay[] STORAGE_DEFAULTS = {
        new SetDisplay(Style.Fill, Color.BLUE)
    };
    
    public static final SetDisplay[] DYNAMIC_DEFAULTS = {
        new SetDisplay(Style.Fill, Color.GREEN),
        new SetDisplay(Style.Fill, Color.BLUE),
        new SetDisplay(Style.Fill, Color.RED),
        new SetDisplay(Style.Fill, Color.YELLOW),
        new SetDisplay(Style.Fill, Color.MAGENTA),
        new SetDisplay(Style.Fill, Color.CYAN),
        new SetDisplay(Style.Fill, Color.ORANGE),
        new SetDisplay(Style.Fill, Color.PINK),
        new SetDisplay(Style.Fill, ColorConstant.aquamarine),
        new SetDisplay(Style.Fill, ColorConstant.blueviolet),
        new SetDisplay(Style.Fill, ColorConstant.brown),
        new SetDisplay(Style.Fill, ColorConstant.burlywood),
        new SetDisplay(Style.Fill, ColorConstant.caderblue),
        new SetDisplay(Style.Fill, ColorConstant.coral),
        new SetDisplay(Style.Fill, ColorConstant.cornflowerblue),
        new SetDisplay(Style.Fill, ColorConstant.darkgoldenrod),
        new SetDisplay(Style.Fill, ColorConstant.darkgreen),
        new SetDisplay(Style.Fill, ColorConstant.darkorange),
        new SetDisplay(Style.Fill, ColorConstant.darkorchid),
        new SetDisplay(Style.Fill, ColorConstant.darksalmon),
        new SetDisplay(Style.Fill, ColorConstant.darkseagreen),
        new SetDisplay(Style.Fill, ColorConstant.darkslateblue),
        new SetDisplay(Style.Fill, ColorConstant.chartreuse),
        new SetDisplay(Style.Fill, ColorConstant.chocolate),
        new SetDisplay(Style.Fill, ColorConstant.darkslategray),
        new SetDisplay(Style.Fill, ColorConstant.darkturquoise),
        new SetDisplay(Style.Fill, ColorConstant.darkviolet),
        new SetDisplay(Style.Fill, ColorConstant.deeppink),
        new SetDisplay(Style.Fill, ColorConstant.deepskyblue),
        new SetDisplay(Style.Fill, ColorConstant.dimgray),
        new SetDisplay(Style.Fill, ColorConstant.dodgerblue),
        new SetDisplay(Style.Fill, ColorConstant.firebrick),
        new SetDisplay(Style.Fill, ColorConstant.forestgreen),
        new SetDisplay(Style.Fill, ColorConstant.gainsboro),
        new SetDisplay(Style.Fill, ColorConstant.gold),
        new SetDisplay(Style.Fill, ColorConstant.darkkhaki)
    };
    
    
    
    public Style style;
    
    public Color color;
    
    /** Creates a new instance of SetDisplay */
    public SetDisplay(Style style, Color color) {
        this.style = style;
        this.color = color;
    }
    
    public static SetDisplay create(SetDisplay original) {
        return new SetDisplay(original.style, original.color);
    }
    
    /*
    public int store() throws DatabaseException {
        return new DatabaseAccess().saveSetDisplay(style, color);
    }
     */
}
