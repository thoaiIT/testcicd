package com.madive.bigcommerce.madiveone.admin.domain;

import java.util.List;
import java.util.Map;

public final class Tree {
	private String text;
	private List<Tree> children;
	private boolean expanded;
	private boolean isRoot;
	public boolean success = true;

	private Map<String, Object> data;

	private Tree() {}

	public static final Tree newTree() {
		return new Tree();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Tree> getChildren() {
		return children;
	}

	public void setChildren(List<Tree> children) {
		this.children = children;
	}

	public boolean isLeaf() {
		if (!isRoot && (children == null || children.isEmpty())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}
}