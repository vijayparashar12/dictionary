package com.vp;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class DictionaryFeatureTest {
	private Dictionary dictionary;

	@Before
	public void init() throws FileNotFoundException {
		dictionary = new Dictionary();
	}

	@Test
	public void validateSourceWords() {
		assertTrue(dictionary.isValidWord("bat"));
		assertTrue(dictionary.isValidWord("bang"));
		assertTrue(dictionary.isValidWord("ant"));
	}

	@Test
	public void partialMatchIsNoMatch() throws Exception {
		assertFalse(dictionary.isValidWord("bangalore"));
		assertFalse(dictionary.isValidWord("bangg"));
		assertFalse(dictionary.isValidWord("nt"));
		assertFalse(dictionary.isValidWord("ba"));
	}

	@Test
	public void noMatchIsNoMatch() throws Exception {
		assertFalse(dictionary.isValidWord("vijay"));
	}

	@Test
	public void verifyWordSuggest() throws Exception {
		List<String> orignal = Arrays.asList(new String[] { "ball", "bat",
				"bang","bus","beautiful" });
		List<String> suggestions = dictionary.suggest("b");
		assertTrue(orignal.size() == suggestions.size()
				&& orignal.containsAll(suggestions));
	}

	@Test
	public void traceTheFinalNodeForPatternSearch() throws Exception {
		Node node = dictionary.getStartingNodes().get('b');
		Node l = node.trace("ball");
		assertTrue(l.equals(new Node(Character.valueOf('l'))));
	}
	
	@Test
	public void verifyfindWordsInaSentence() throws Exception {
		List<String> words = dictionary.findWordInSentence("i love bat and ball");
		assertTrue(words.size()==2);
		assertTrue(words.contains("bat"));
		assertTrue(words.contains("ball"));
		assertTrue(dictionary.findWordInSentence("bappleq").contains("apple"));
	}
}
