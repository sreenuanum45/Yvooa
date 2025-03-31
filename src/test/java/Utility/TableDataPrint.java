package Utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TableDataPrint {
	public void EachColoumTextPrint(List<WebElement> tables) throws InterruptedException {

		System.out.println("tables in page:" + tables.size());
		for (int i = 0; i < tables.size(); i++) {
			Thread.sleep(1000);
			List<WebElement> rows = tables.get(i).findElements(By.xpath("child::tr"));
			System.out.println(rows.size() + "rows in each table" + i);
			System.out.println("===========");
			for (int j = 0; j < rows.size(); j++) {
				Thread.sleep(1000);
				List<WebElement> coloum = rows.get(j).findElements(By.xpath("child::td"));
				System.out.println(coloum.size() + "coloum in each row is" + j);
				for (int k = 0; k < coloum.size(); k++) {
					Thread.sleep(2000);
					if (k < coloum.size() - 1) {
						System.out.print(coloum.get(k).getText() + "  , ");
					} else {
						System.out.print(coloum.get(k).getText());

					}

				}
				System.out.println();

			}
		}
	}

	public void tableSizeCheck(List<WebElement> tables) {
		System.out.println("number of table in the page" + tables.size());

		for (WebElement row : tables) {
			List<WebElement> rows = row.findElements(By.xpath("child::tr"));
			System.out.println("number of rows in the table:" + rows.size());
			System.out.println("-----------");
			for (WebElement colums : rows) {
				List<WebElement> colum = colums.findElements(By.xpath("child::td"));
				System.out.println("number of coloum in the table:" + colum.size());
			}
		}
	}
}