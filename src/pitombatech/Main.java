package pitombatech;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

	// the main parts for create each bibtex
	static String author, title, journal, volume, issue, pages, month, year, abst, keywords, address, comment;
	static String texName;
	static int quantity = 1;
	static FileReader fileReader;
	static BufferedReader bufferedReader;
	static FileWriter fileWriter;
	static BufferedWriter bufferedWriter;
	static boolean startDocument = false; // to avoid first break line

	/**
	 * Input args must be the file medline, for exemplo: file.med
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		clearTypes();

		String fileName = args[0];

		fileReader = new FileReader(fileName);
		bufferedReader = new BufferedReader(fileReader);
		fileWriter = new FileWriter("result.bibtex");
		bufferedWriter = new BufferedWriter(fileWriter);

		String line = bufferedReader.readLine();
		String lastType = null;
		while (line != null) {
			if (!line.isEmpty()) {
				String currentType = line.substring(0, 3);
				selectType(line, currentType, lastType);
				// currentType and lastType fix break lines in abstracts and
				// address
				if (!currentType.equals("   "))
					lastType = currentType;

				startDocument = true;

			} else {
				// empty line, that means a new bibtex and close the last one
				if (startDocument == true)
					setTypes();
			}
			line = bufferedReader.readLine();

		}
		setTypes();

		bufferedReader.close();
		fileReader.close();
		bufferedWriter.close();
		fileWriter.close();

	}

	private static void setTypes() throws IOException {
		texName = year + quantity;
		String[] array = new String[13];
		array[0] = author;
		array[1] = title;
		array[2] = journal;
		array[3] = volume;
		array[4] = issue;
		array[5] = pages;
		array[6] = month;
		array[7] = year;
		array[8] = abst;
		array[9] = keywords;
		array[10] = address;
		array[11] = comment;
		array[12] = texName;
		bufferedWriter.write(printBibtex(array));
		clearTypes();
		System.out.println("Number:" + quantity);
		quantity++;
	}

	private static void clearTypes() {
		author = "";
		title = "";
		journal = "";
		volume = "";
		issue = "";
		pages = "";
		month = "";
		year = "";
		abst = "";
		keywords = "";
		address = "";
		comment = "";

	}

	public static void selectType(String line, String currentType, String lastType) {
		switch (currentType) {
		case "AU ":
			if (author != null) {
				if (author.isEmpty())
					author = line.substring(6).split(" ", 2)[0] + ", "
							+ line.substring(6).split(" ", 2)[1].substring(0, 1) + ".";
				else
					author = author + " and " + line.substring(6).split(" ", 2)[0] + ", "
							+ line.substring(6).split(" ", 2)[1].substring(0, 1) + ".";
			}
			break;
		case "TI ":
			title = line.substring(6);
			break;
		case "JT ":
			journal = line.substring(6);
			break;
		case "VI ":
			volume = line.substring(6);
			break;
		case "IP ":
			issue = line.substring(6); // number
			break;
		case "PG ":
			pages = line.substring(6);
			break;
		case "DP ":
			if (line.length() > 10)
				month = line.substring(11, 14);
			year = line.substring(6, 10);
			break;
		case "AB ":
			if (abst != null && abst.isEmpty())
				abst = line.substring(6);
			else if (abst != null && !abst.isEmpty())
				abst = abst + " " + line.substring(6);
			break;
		case "OT ":
			keywords = line.substring(6);
			break;
		case "AD ":
			if (address != null && address.isEmpty())
				address = line.substring(6);
			else if (address != null && !address.isEmpty())
				address = address + " " + line.substring(6);
			break;
		case "SO ":
			comment = line.substring(6);
			break;
		case "   ":
			selectType(line, lastType, lastType);
			break;
		default:
		}

	}

	public static String printBibtex(String[] array) {

		String out = "" + "@Article{" + texName + "," + "\n" + "author  = {" + array[0] + "},\n" + "title  = {"
				+ array[1] + "},\n" + "journal  = {" + array[2] + "},\n" + "volume  = {" + array[3] + "},\n"
				+ "number  = {" + array[4] + "},\n" + "pages  = {" + array[5] + "},\n" + "year  = {" + array[7] + "},\n"
				+ "abstract  = {" + array[8] + "},\n" + "address  = {" + array[10] + "},\n" + "note  = {" + array[11]
				+ "}\n}\n\n";
		// System.out.println(out);
		return out;
	}

}
