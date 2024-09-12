package com.zhou.tetris.layout;

import java.awt.Component;
import java.awt.Container;

public class MainLayout extends LayoutAdapter {

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void layoutContainer(Container parent) {
		var cs = parent.getComponents();
		cs[0].setBounds(0, 0, 100, 440);
		cs[1].setBounds(200, 0, 200, 440);
		cs[2].setBounds(600, 0, 100, 440);
	}

}
