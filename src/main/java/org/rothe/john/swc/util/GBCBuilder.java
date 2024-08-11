package org.rothe.john.swc.util;

import java.awt.GridBagConstraints;
import java.awt.Insets;

public class GBCBuilder {
    private int gridx = -1;
    private int gridy = -1;
    private int gridwidth = 1;
    private int gridheight = 1;
    private double weightx = 0.0;
    private double weighty = 0.0;
    private int anchor = GridBagConstraints.CENTER;
    private int fill = GridBagConstraints.NONE;
    private Insets insets = new Insets(0,0,0,0);
    private int ipadx;
    private int ipady;
    
    public GBCBuilder gridx(int gridx) {
        this.gridx = gridx;
        return this;
    }

    public GBCBuilder gridy(int gridy) {
        this.gridy = gridy;
        return this;
    }

    public GBCBuilder gridwidth(int gridwidth) {
        this.gridwidth = gridwidth;
        return this;
    }

    public GBCBuilder gridheight(int gridheight) {
        this.gridheight = gridheight;
        return this;
    }

    public GBCBuilder weightx(double weightx) {
        this.weightx = weightx;
        return this;
    }

    public GBCBuilder weighty(double weighty) {
        this.weighty = weighty;
        return this;
    }

    public GBCBuilder anchor(int anchor) {
        this.anchor = anchor;
        return this;
    }

    public GBCBuilder anchorWest() {
        this.anchor = GridBagConstraints.WEST;
        return this;
    }

    public GBCBuilder anchorEast() {
        this.anchor = GridBagConstraints.EAST;
        return this;
    }

    public GBCBuilder anchorNorth() {
        this.anchor = GridBagConstraints.NORTH;
        return this;
    }

    public GBCBuilder anchorSouth() {
        this.anchor = GridBagConstraints.SOUTH;
        return this;
    }

    public GBCBuilder fill(int fill) {
        this.fill = fill;
        return this;
    }

    public GBCBuilder fillHorizontal() {
        this.fill = GridBagConstraints.HORIZONTAL;
        return this;
    }

    public GBCBuilder fillVertical() {
        this.fill = GridBagConstraints.VERTICAL;
        return this;
    }

    public GBCBuilder fillBoth() {
        this.fill = GridBagConstraints.BOTH;
        return this;
    }

    public GBCBuilder fillNone() {
        this.fill = GridBagConstraints.NONE;
        return this;
    }

    public GBCBuilder insets(int top, int left, int bottom, int right) {
        this.insets = new Insets(top, left, bottom, right);
        return this;
    }
    public GBCBuilder insets(int amount) {
        this.insets = new Insets(amount, amount, amount, amount);
        return this;
    }

    public GBCBuilder insets(Insets insets) {
        this.insets = (Insets)insets.clone();
        return this;
    }

    public GBCBuilder ipadx(int ipadx) {
        this.ipadx = ipadx;
        return this;
    }

    public GBCBuilder ipady(int ipady) {
        this.ipady = ipady;
        return this;
    }

    public GridBagConstraints build() {
        return new GridBagConstraints(gridx, gridy, gridwidth, gridheight,  weightx, weighty,
                anchor, fill, insets, ipadx, ipady);
    }
}
