package com.leeloo.tbe.isbn;

import java.io.PrintWriter;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.leeloo.tbe.rest.jsonpojos.UiBook;

public class EsLookupService {

	public UiBook findBook(String isbn) throws Exception
	{
		
		String searchUrl = "http://www.casadellibro.com/buscador/busquedaGenerica?busqueda="+isbn;
		 
		Document searchResult = Jsoup.connect(searchUrl)
				  .data("query", "Java")
				  .userAgent("Mozilla")
				  .cookie("auth", "token")
				  .timeout(7000)
				  .post();			
		
		PrintWriter out = new PrintWriter("c:/dev/search.html");
		out.println(searchResult.html());
		out.close();
		
		Elements elm = searchResult.select("a.title-link.searchResult");		
		if(elm == null){
			throw new Exception(String.format("Casadellibro does not have the book isbn %s", isbn));
		}		
		String productUrl = elm.attr("href");
		String produclUrl = "http://www.casadellibro.com" + productUrl;
		
		Document doc = Jsoup.connect(produclUrl)
				  .data("query", "Java")
				  .userAgent("Mozilla")
				  .cookie("auth", "token")
				  .timeout(7000)
				  .post();
		out = new PrintWriter("c:/dev/book.html");
		out.println(doc.html());
		out.close();
		
		CasadelLibroParser parser = new CasadelLibroParser();
		parser.parse(doc);
		
		UiBook uiBook = new UiBook();
		
		uiBook.ownedByCurrentUser=true;		
		uiBook.title = parser.getTitle();
		uiBook.authors = parser.getAuthors();
		uiBook.description = parser.getDescription();
		uiBook.language=parser.getLanguage();
		uiBook.pageCount = parser.getPageCount();
		uiBook.imageUrl = parser.getImageLink();		
		uiBook.hasImage=parser.hasImage();		
		uiBook.isbn = isbn;
		uiBook.categories= parser.getCategories();

		return uiBook;
	}
		
}

