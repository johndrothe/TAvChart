package org.rothe.john.working_hours.ui.canvas.mouse;

import lombok.RequiredArgsConstructor;
import org.rothe.john.working_hours.ui.canvas.util.CanvasCalculator;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

@RequiredArgsConstructor
public class CanvasMouseListener implements MouseListener, MouseMotionListener {
    private final CanvasCalculator calculator;

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    protected void register(JComponent component) {
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    protected CanvasCalculator calculator() {
        return calculator;
    }
}
