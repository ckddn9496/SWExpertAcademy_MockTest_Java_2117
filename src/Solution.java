import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class Solution {

	static int N, M;
	static int[][] map;
	static int maxHouse;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

		int T = Integer.parseInt(br.readLine());

		for (int test_case = 1; test_case <= T; test_case++) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			map = new int[5 * N][5 * N];
			
			maxHouse = 1;
			int numOfHouse = 0;
			for (int i = 2 * N; i < 3 * N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 2 * N; j < 3 * N; j++) {
					map[i][j] = Integer.parseInt(st.nextToken());
					if (map[i][j] == 1) {
						numOfHouse++;
					}
				}
			}

			if (numOfHouse == 1) {
				bw.append("#" + test_case + " 1\n");
				continue;
			}

			int maxProfit = M * numOfHouse;
			int k = 1;
			while (true) {
				if (computeCost(k + 1) <= maxProfit)
					k++;
				else
					break;
			}

			for (int kIdx = k; kIdx > 0; kIdx--) {
				for (int i = 2 * N; i < 3 * N; i++) {
					for (int j = 2 * N; j < 3 * N; j++) {
						setSecurityCorp(i, j, kIdx);
					}
				}
			}

			bw.append("#" + test_case + " "+maxHouse+"\n");
		}
		bw.flush();
		bw.close();
		br.close();
	}

	private static void setSecurityCorp(int x, int y, int k) {
		int n = 0;
		int count = 0;
		for (int i = x - k + 1; i < x; i++) {
			for (int j = y - n; j <= y + n; j++) {
				if (map[i][j] == 1)
					count++;
			}
			n++;
		}

		for (int i = x; i < x + k; i++) {
			for (int j = y - n; j <= y + n; j++) {
				if (map[i][j] == 1)
					count++;
			}
			n--;
		}

		if (count * M >= computeCost(k)) {
			if (maxHouse < count) {
				maxHouse = count;				
			}
		}
	}

	private static int computeCost(int k) {
		return (2 * (k - 1) * k + 1);
	}
	
	private static void printMap(int[][] temp) {
		for (int[] row : temp) {
			for (int i : row) {
				System.out.print(i + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
