package ua.alex.gen.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ua.alex.gen.utilus.SimplePoint;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(name = "space")
public class XmlWorld {
	@XmlElement
	//@XmlElementWrapper(name = "nodes")
	private XmlComponentNode[] node;
	
	private List<XmlComponentNode> tmpComponents = new ArrayList<XmlComponentNode>();

	public void clear() {
		tmpComponents.clear();
	}
	
	public void add(Component component, int x, int y) {
		tmpComponents.add(new XmlComponentNode(component, x, y));
	}
	
	public void confirm() {
		node = new XmlComponentNode[tmpComponents.size()];
		for (int i = 0; i < node.length; i++) {
			node[i] = tmpComponents.get(i);
		}
	}
	
	public int getCount() {
		return node.length;
	}
	
	public Component getComponent(int index) {
		return node[index].create();
	}
	
	public SimplePoint getPosition(int index) {
		return node[index].getPostition();
	}
}
