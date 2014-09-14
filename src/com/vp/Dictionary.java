package com.vp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

public class Dictionary {
	private Map<Character, Node> startingNodes;

	public Dictionary() throws FileNotFoundException {
		build();
	}

	private void build() throws FileNotFoundException {
		startingNodes = new HashMap<Character, Node>();
		Scanner scanner = new Scanner(new File("source.txt"));
		while (scanner.hasNext()) {
			String word = scanner.nextLine();
			Character firstChar = word.charAt(0);
			Node firstNode = null;
			if (startingNodes.containsKey(firstChar)) {
				firstNode = startingNodes.get(firstChar);
			} else {
				firstNode = new Node(firstChar);
				startingNodes.put(firstChar, firstNode);
			}
			Node previous = firstNode;
			for (int i = 1; i < word.length(); i++) {
				char nextChar = word.charAt(i);
				Node next = null;
				if (previous.hasConnection(nextChar)) {
					next = previous.getNextNode(nextChar);
				} else {
					next = new Node(nextChar);
					previous.addEdge(next);
				}
				if (i == (word.length() - 1)) {
					next.setTerminatingCharacter(true);
				}
				previous = next;
			}
		}
		scanner.close();
	}

	public boolean isValidWord(String word) {
		Character firstCharacter = word.charAt(0);
		if (startingNodes.containsKey(firstCharacter)) {
			Node node = startingNodes.get(firstCharacter);
			for (int i = 1; i < word.length(); i++) {
				Character nextChar = word.charAt(i);
				if (node.hasConnection(nextChar)) {
					node = node.getNextNode(nextChar);
				} else {
					return false;
				}
			}
			return node != null && node.isTerminatingCharacter();
		}
		return false;
	}

	protected List<String> recursiveSuggest(Node entryPoint, String pattern) {
		Stack<Node> stack = new Stack<Node>();
		List<String> suggestions = new LinkedList<String>();
		if (!entryPoint.isTerminatingCharacter()) {
			if (!entryPoint.isLeafNode()) {
				Set<Node> nextCharacters = entryPoint.getAllNextCharacters();
				nextCharacters.forEach(stack::push);
			}
		} else {
			suggestions.add(pattern);
		}
		while (!stack.isEmpty()) {
			Node current = stack.pop();
			suggestions.addAll(recursiveSuggest(current,
					pattern + current.value()));
		}
		return suggestions;
	}

	public List<String> suggest(String pattern) {
		Stack<Node> stack = new Stack<Node>();
		List<String> suggestions = new ArrayList<String>();
		Character firstChar = pattern.charAt(0);
		if (startingNodes.containsKey(firstChar)) {
			Node firstNode = startingNodes.get(firstChar);
			Node entryPoint = firstNode.trace(pattern);
			Set<Node> nextCharacters = entryPoint.getAllNextCharacters();
			nextCharacters.forEach(stack::push);
			while (!stack.isEmpty()) {
				Node next = stack.pop();
				String item = pattern + next.value();
				suggestions.addAll(recursiveSuggest(next, item));
			}
		}
		return suggestions;
	}

	public Map<Character, Node> getStartingNodes() {
		return startingNodes;
	}

	public List<String> findWordInSentence(String sentence) {
		List<String> words = new ArrayList<String>();
		Node node = null;
		int start = -1;
		for (int i = 0; i < sentence.length(); i++) {
			char charAt = sentence.charAt(i);
			if (node != null) {
				if (node.hasConnection(charAt)) {
					node = node.getNextNode(charAt);
					if (node.isTerminatingCharacter()) {
						words.add(sentence.substring(start, i + 1));
						start = -1;
						node = null;
					}
				} else {
					if (startingNodes.containsKey(node.value())) {
						node = startingNodes.get(node.value());
						if (node.hasConnection(charAt)) {
							start = i - 1;
							node = node.getNextNode(charAt);
						}

					} else {
						node = null;
						start = -1;
					}
				}
			} else if (startingNodes.containsKey(charAt)) {
				node = startingNodes.get(charAt);
				start = i;
			}
		}
		return words;
	}
}
