package com.badr.contactmanagement.nlp;
import org.apache.commons.text.similarity.LevenshteinDistance;
import java.util.regex.Pattern;

public class NlpClass {
	
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
		if (str == null) {
	        return false; 
	    }
	    return pattern.matcher(str).matches();
	}
	
	public static int  minEditDistance(String conactNom, String searchInput) {
		int m = conactNom.length();
        int n = searchInput.length();

        int[][] dp = new int[m + 1][n + 1];

        // Initialize the first row and column
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Calculate the edit distance
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (conactNom.charAt(i - 1) == searchInput.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int replace = dp[i - 1][j - 1] + 1;
                    int insert = dp[i][j - 1] + 1;
                    int delete = dp[i - 1][j] + 1;

                    dp[i][j] = Math.min(Math.min(replace, insert), delete);
                }
            }
        }

        return dp[m][n];
		
	}
}
