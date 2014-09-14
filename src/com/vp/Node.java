package com.vp;

import java.util.Set;
import java.util.TreeSet;

public class Node {

	private Character value;
	private Set<Node> edges;
	private boolean terminatingCharacter;

	public Node(Character ch) {
		this.value = ch;
	}

	public void addEdge(Node connection) {
		if (edges == null) {
			edges = new TreeSet<Node>((n1, n2) -> n1.value().compareTo(
					n2.value()));
		}
		edges.add(connection);
	}

	public Set<Node> getAllNextCharacters() {
		return this.edges;
	}

	public boolean isLeafNode() {
		return this.edges == null ? true : false;
	}

	public boolean hasConnection(final Character next) {
		if (edges != null) {
			return edges.stream().filter(node -> node.value().equals(next))
					.findFirst().isPresent();
		}
		return false;
	}

	public Character value() {
		return value;
	}

	public Node getNextNode(final Character next) {
		return edges.stream().filter(node -> node.value().equals(next))
				.findFirst().get();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(value).append("]");
		/*
		 * if (edges != null) { sb.append(edges); }
		 */
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node node = (Node) obj;
			return this.value.equals(node.value());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	public boolean isTerminatingCharacter() {
		return terminatingCharacter;
	}

	public void setTerminatingCharacter(boolean terminatingCharacter) {
		this.terminatingCharacter = terminatingCharacter;
	}

	public Node trace(String pattern) {
		Node node = this, next = null;
		if (value.equals(pattern.charAt(0))) {
			for (int i = 1; i < pattern.length(); i++) {
				if (node.hasConnection(pattern.charAt(i))) {
					next = node.getNextNode(pattern.charAt(i));
					node = next;
				} else {
					return null;
				}
			}
			return node;
		}
		return null;
	}
}
