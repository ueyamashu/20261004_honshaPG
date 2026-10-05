package LIP.dto;

public class TBLIPFileInDTO {
	// 入力filename
	private String inputfilename;
	// 抽出filename
	private String outputfilename;
	// 資料ID
    private int bookId;
    // ISBN番号
    private String isbn;
    // 出版日
    private String arrivalDate;
    

    
	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public String getArrivalDate() {
		return arrivalDate;
	}

	public void setArrivalDate(String arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}	

	public String getInputfilename() {
		return inputfilename;
	}

	public void setInputfilename(String inputfilename) {
		this.inputfilename = inputfilename;
	}

	public String getOutputfilename() {
		return outputfilename;
	}

	public void setOutputfilename(String outputfilename) {
		this.outputfilename = outputfilename;
	}

	@Override
    public String toString() {
        return bookId + "," + isbn + "," + arrivalDate + "\r\n";
    }
}
