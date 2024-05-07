/**
*
* @author Enise Bihter Sarı b191210101@sakarya.edu.tr
* @since 05.04.2024
* <p>
* Bu sınıf çektiğimiz java sınıflarındaki javadoc, yorum, kod satırı,loc ve fonksiyon sayısını bulur. 
* Bu sayılar üzerinden yorum sapma yüzdesini hesaplayarak ekrana yazdırır.
* </p>
*/



package pdpProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class JavaFileAnalyzer {
	public static void analyzeJavaFile(String filePath) throws IOException {
	    BufferedReader reader = new BufferedReader(new FileReader(filePath));
	    String line;
	    boolean inJavadoc = false;
	    int javadocLines = 0;   	//javadoc yorum sayısını tutar
	    int otherCommentLines = 0;  //single ve multiline olan, javadoc dışı yorumların sayısını tutar
	    int codeLines = 0;  		//boşlukları silince kalan ham kod yazı satır sayısını tutar
	    int totalLines = 0;  		//boşluklar da dahil kod satır sayısını tutar
	    int functionCount = 0; 	    //fonksiyon sayısını tutar

	    Pattern singleLineCommentPattern = Pattern.compile("^\\s*//.*$"); // tekli yorum satırı, 77. satırda tanımladığım regexten farkı bunun tüm satırı yorum kabul etmesidir.
	    Pattern multiLineCommentStartPattern = Pattern.compile("^\\s*/\\*.*"); // şu /* şekilde başlayan multiline yorum satırı
	    Pattern multiLineCommentEndPattern = Pattern.compile(".*\\*/\\s*$"); // şu */ şekilde biten multiline yorum satırı
	   

	    while ((line = reader.readLine()) != null) { 
	        totalLines++;
	        line = line.trim();
	        Matcher matcher; //matcher yardımı ile regexleri teker teker yazmayacağım

	        if (line.startsWith("/**")) {  //başlangıcı /** ile başlayanlar javadoctur
	            inJavadoc = true;
	        } else if (line.startsWith("*/")) {
	            inJavadoc = false;
	        } else if (inJavadoc || line.startsWith("*")) { // javadocta * ile satır başı yaptığımız yerleri dahil ederek sayar
	            javadocLines++;
	        } else if ((matcher = singleLineCommentPattern.matcher(line)).find()) {
	            otherCommentLines++; 	// tek satır yorumlar için arttırır
	        }else if ((matcher = multiLineCommentStartPattern.matcher(line)).find()) {
	        							//çok satırlı yorum başlangıcına girdik
	            if (!line.endsWith("*/")) {
	                while ((line = reader.readLine()) != null) {
	                    totalLines++;
	                    if ((matcher = multiLineCommentEndPattern.matcher(line)).find()) {
	                    				//çoklu yorum satırının bitişini ifade eder
	                        break;
	                    } else if(!line.endsWith("//")) {
	                    	otherCommentLines++;
	                    }	
	                }
	            }
	        }
	        else if (!line.isEmpty()) {
	            codeLines++;
	            if (line.matches(".*\\s+[^\\s]+\\(.*\\)\\s*\\{?\\s*")) {//fonksiyonları bulmak için gerekli olan regex kodu
	                functionCount++;
	            }
	            if (line.matches("^\\s*//.*|.*\\s*//.*")) {  // başında string olsa da sonradan tanımlanmış  // tekli yorumu görür
	                otherCommentLines++;
	            }
	        }
	    }

	    // Yorum sapma Yüzdesini hesaplar
	    double YG = ((javadocLines + otherCommentLines) * 0.8) / functionCount;
	    double YH = (codeLines / (double) functionCount) * 0.3;
	    double commentDeviationPercentage = (100 * YG) / YH - 100;
	    DecimalFormat df = new DecimalFormat("#.##");  // virgülden sonra sadece 2 sayı gözükecek şekilde ayarlar

	    System.out.println("Sınıf: " + filePath.substring(filePath.lastIndexOf(File.separator) + 1)); //içinde olduğumuz dosya sınıfı için gerekli işlemler uygulanır
	    System.out.println("Javadoc Satır Sayısı: " + javadocLines);
	    System.out.println("Yorum Satır Sayısı: " + otherCommentLines);
	    System.out.println("Kod Satır Sayısı: " + codeLines);
	    System.out.println("LOC: " + totalLines);
	    System.out.println("Fonksiyon Sayısı: " + functionCount);
	    System.out.println("Yorum Sapma Yüzdesi: % " + df.format(commentDeviationPercentage));
	    System.out.println("-----------------------------------------");
	    reader.close();
	}

}