# SWExpertAcademy_MockTest_Java_2117

## SW Expert Academy 2117. [모의 SW 역량테스트] 홈 방범 서비스

### 1. 문제설명

출처: https://swexpertacademy.com/main/code/problem/problemDetail.do?contestProbId=AV5V61LqAf8DFAWu

input으로 지형의 한변의 길이 `N`, 집이 지불할 요금 `M`가 들어온다. 이어서 `N*N` 지도의 정보가 들어온다. 지도의 정보에서 `1`은 집을 뜻하며  반경 `k`의 방법서비스를 제공한다. 홈 방범 서비스는 마름모로 한변의 길이가 `k`를 뜻하며 설치 비용은 서비스 제공 면적만큼이다. 최대한 많은 집에 방범서비스를 제공하면서 손해가 나지않게 서비스를 제공하려고 할때 제공할 수 있는 집의 최대 수를 출력하는 문제.

[제약사항]

    1. 시간제한 : 최대 50개 테스트 케이스를 모두 통과하는데, C/C++/Java 모두 3초
    2. 도시의 크기 N은 5 이상 20 이하의 정수이다. (5 ≤ N ≤ 20)
    3. 하나의 집이 지불할 수 있는 비용 M은 1 이상 10 이하의 정수이다. (1 ≤ M ≤ 10)
    4. 홈방범 서비스의 운영 비용은 서비스 영역의 면적과 동일하다.
    5. 도시의 정보에서 집이 있는 위치는 1이고, 나머지는 0이다.
    6. 도시에는 최소 1개 이상의 집이 존재한다.


[입력]

> 입력의 맨 첫 줄에는 총 테스트 케이스의 개수 `T`가 주어지고, 그 다음 줄부터 `T`개의 테스트 케이스가 주어진다.
> 각 테스트 케이스의 첫 번째 줄에는 도시의 크기 `N`과 하나의 집이 지불할 수 있는 비용 `M`이 주어진다.
> 그 다음 줄부터 `N*N` 크기의 도시정보가 주어진다. 지도에서 `1`은 집이 위치한 곳이고, 나머지는 `0`이다.


[출력]

> 테스트 케이스의 개수만큼 `T`줄에 `T`개의 테스트 케이스 각각에 대한 답을 출력한다.
> 각 줄은 `#x`로 시작하고 공백을 하나 둔 다음 정답을 출력한다. (`x`는 `1`부터 시작하는 테스트 케이스의 번호이다)
> 출력해야 할 정답은 손해를 보지 않으면서 홈방범 서비스를 가장 많은 집들에 제공하는 서비스 영역을 찾았을 때, 그 때의 서비스를 제공 받는 집들의 수이다.

### 2. 풀이

Brute Force 방법을 이용하였다. 방범소의 중심좌표와 반경`k`값을 받으면 지도에서 해당구역 내 존재하는 집의 수를 카운트하여 `M`만큼 곱하여 얻을 수 있는 수입과 서비스 반경의 값을 뺀것이 `0`보다 크며 이때 제공할 수 있는 집의 갯수가 최대로 제공할 수 있는 집의 수보다 크다면 최댓값을 갱신하며 답을 찾았다. 반경 `k`에 대해서 발생하는 비용은 계차수열의 형태를 띄어 일반식으로 `k`에대해 비용을 계산하는 식을 만들어 사용해주었다.

방범소의 범위를 지도에 적용하면서 필터링하는 과정에서 지도의 크기를 넘어가는것에 대해 문제가 발생할 수 있다. 이를 위해 약간의 트릭을 추가하였다. 배열의 크기를 최대 지도 `N * N`을 담을 수 있는 크기(`[5*N][5*N]`)로 잡아 가운데 input으로 들어온 지도의 값을 넣어주고 더 큰 지도에서 필터링을 진행하여 인덱스 걱정없이 적용되는 집의 수를 카운트 할 수 있었다.


```java

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


```

