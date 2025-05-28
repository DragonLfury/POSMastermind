/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lexso.util;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class PanelRounderUtility {

    /**
     * A custom Border that draws a rounded rectangle as the background of the
     * component it is applied to, using the component's own background color.
     */
    private static class DynamicRoundedCornerBorder extends AbstractBorder {

        private int cornerRadius;

        // Constructor now only takes the radius
        public DynamicRoundedCornerBorder(int radius) {
            this.cornerRadius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            super.paintBorder(c, g, x, y, width, height);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Get the component's actual background color
            Color componentBgColor = c.getBackground();

            // Create the rounded rectangle shape
            RoundRectangle2D.Float roundRect = new RoundRectangle2D.Float(
                    x, y, width - 1, height - 1, cornerRadius, cornerRadius
            );

            // Fill the rounded background using the component's background color
            g2.setColor(componentBgColor);
            g2.fill(roundRect);

            // Optional: Draw a thin border outline using a slightly darker shade
            g2.setColor(componentBgColor.darker());
            g2.setStroke(new BasicStroke(1));
            g2.draw(roundRect);

            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            // Provide appropriate insets to prevent content drawing over rounded corners
            int inset = cornerRadius / 2;
            return new Insets(inset, inset, inset, inset);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = cornerRadius / 2;
            return insets;
        }
    }

    /**
     * Applies rounded corners to an existing JPanel, using its current
     * background color and dimensions.
     *
     * @param panel The JPanel instance to make rounded.
     * @param radius The radius for the corners.
     */
    public static void applyRoundedCorners(JPanel panel, int radius) {
        // Set the custom border. The border will handle drawing the rounded shape
        // using the panel's current background color.
        panel.setBorder(new DynamicRoundedCornerBorder(radius));

        // Critically, set the panel to be non-opaque. This prevents the JPanel's
        // default rectangular background from being drawn over our custom rounded border.
        panel.setOpaque(false);
    }
}
