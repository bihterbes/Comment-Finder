/**
*
* @author Enise Bihter Sarı b191210101@sakarya.edu.tr
* @since 04.04.2024
* <p>
* Bu sınıf verilen github linkindeki dosyaları klonlar ve sadece java uzantılı olanları çeker. Aynı zamanda sınıf mı değil mi diye kontrol eder.
* </p>
*/

package pdpProject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RepsitoryAnalyzer {
	
	public static void runAnalyzer() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("GitHub repository link giriniz: (https://github.com/user/projectName)");
            String repoLink = reader.readLine();
      
            // ".git" uzantısını ve "/" karakterini kaldırma böylece hata vermez
            if (repoLink.endsWith(".git")) {
                repoLink = repoLink.substring(0, repoLink.length() - 4);
            }
            
            if (repoLink.endsWith("/")) {
                repoLink = repoLink.substring(0, repoLink.length() - 1);
            }
            // repository klonlama işlemi
            ProcessBuilder builder = new ProcessBuilder("git", "clone", repoLink);
            Process process = builder.start();
            process.waitFor();

            // dosyaları alma
            File directory = new File(repoLink.substring(repoLink.lastIndexOf("/") + 1));
            analyzeFiles(directory);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	// java uzantılı ve class olan dosyaları alır
    private static void analyzeFiles(File file) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    analyzeFiles(f);
                }
            }
        } else if (file.getName().endsWith(".java")) {
        	 if (isClassFile(file)) {
                 JavaFileAnalyzer.analyzeJavaFile(file.getAbsolutePath());
             }
        }
    }
    
    //bir dosya interface mi class mı değil mi anlamamızı sağlar
    private static boolean isClassFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        boolean isClassFile = false;

        try {
            while ((line = reader.readLine()) != null) {
                if (line.contains("class ")) {
                    isClassFile = true;
                    break;
                }
            }
        } finally {
            reader.close();
        }

        return isClassFile;
    }
    
    

}
