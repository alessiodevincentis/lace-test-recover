package com.woonders.lacemsjsf.view.pdf;

import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;

public class SelectPages {
	HashMap<PDPage, Integer> hashPage = new HashMap<>();

	private void initHashMap(PDPageTree pages) {

		int i = 1;
		for (PDPage page : pages) {
			hashPage.put(page, i);
			i++;
		}

	}

	public void addPages(PDDocument document, PDPageTree pages, Integer[] val) {

		initHashMap(pages);

		int i = 0;
		for (PDPage page : pages) {
			if (hashPage.get(page) == val[i]) {
				document.addPage(page);
				i++;
			}
			if (!(i < val.length))
				break;
		}
	}
}
