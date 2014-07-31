package es.jshcd.lectorrss.model;

/**
 * This class represents a news item.
 * 
 * @author Javier Sanchez Hernandez
 * @version 1.0
 */
public class News {
	private String title;
	private String description;
	private String content;
	private String imageUrl;
	private String link;
	
	
	
	public News() {
		this.title = "";
		this.description = "";
		this.content = "";
		this.imageUrl = "";
		this.link = "";
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param title
	 * @param description
	 * @param content
	 * @param imageUrl
	 * @param link
	 */
	public News(String title, String description, String content, String imageUrl, String link) {
		this.title = title;
		this.description = description;
		this.content = content;
		this.imageUrl = imageUrl;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
