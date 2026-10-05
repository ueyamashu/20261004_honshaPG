package BIE.dto;

public class TBBIEFileOutDTO {
	// filename
	private String filename;
	// 資料ID
    private int bookId;
    // ISBN番号
    private String isbn;
    // 資料名
    private String title;
    // 著者名
    private String author;
    // 出版社
    private String publisher;
    // 出版日
    private String releaseDate;
	// 重複冊数
    private int book_cnt;
    
	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	
    public int getBook_cnt() {
		return book_cnt;
	}

	public void setBook_cnt(int book_cnt) {
		this.book_cnt = book_cnt;
	}

	
	/**
	 * @return filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename セットする filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	

	@Override
    public String toString() {
        return isbn + "," + title + "," + author + "," + publisher + "," + releaseDate + "," + book_cnt + "\r\n";
    }
}
