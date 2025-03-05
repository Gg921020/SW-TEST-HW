import java.util.*;


class Team implements Comparable<Team> {
    String name;
    String league; // 聯盟: AL 或 NL
    String division; // 分區: East, Central, West
    int wins;
    int losses;
    int seed;

    //初始化球隊資訊，包含防錯機制
    public Team(String name, String league, String division, int wins, int losses) {
        if (name == null || league == null || division == null || wins < 0 || losses < 0) {
            throw new IllegalArgumentException("Invalid team data");
        }
        this.name = name;
        this.league = league;
        this.division = division;
        this.wins = wins;
        this.losses = losses;
    }

    // 計算勝率
    public double getWinRate() {
        return (double) wins / (wins + losses);
    }

    // 比較方法，用於排序球隊，勝率高者排前面
    @Override
    public int compareTo(Team other) {
        return Double.compare(other.getWinRate(), this.getWinRate());
    }


}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Team> alTeams = new ArrayList<>();
        List<Team> nlTeams = new ArrayList<>();

        int totalGames = 0;

        for (int i = 0; i < 30; i++) {
            try {
                String[] input = scanner.nextLine().split(" ");
                if (input.length != 5) {
                    throw new IllegalArgumentException("Invalid input format. Expected: TeamName League Division Wins Losses");
                }

                String teamName = input[0];
                String league = input[1];
                String division = input[2];
                int wins = Integer.parseInt(input[3]);
                int losses = Integer.parseInt(input[4]);

                // 檢查總場次是否一致
                totalGames += wins + losses;

                Team team = new Team(teamName, league, division, wins, losses);

                if (league.equals("AL")) {
                    alTeams.add(team);
                } else if (league.equals("NL")) {
                    nlTeams.add(team);
                } else {
                    throw new IllegalArgumentException("Invalid league input. Must be AL or NL.");
                }
            } catch (Exception e) {
                System.out.println("Error processing input: " + e.getMessage());
                i--; // 重新輸入該隊資訊
            }
        }

        // 確保總場次一致（每場比賽應該有兩支球隊參與）
        assert totalGames % 2 == 0 : "Total games played should be even, indicating every game has a winner and a loser.";

        // 選出分區冠軍
        Map<String, Team> alChampions = getChampions(alTeams);
        Map<String, Team> nlChampions = getChampions(nlTeams);

        List<Team> alChampionTeams = new ArrayList<>(alChampions.values());
        List<Team> nlChampionTeams = new ArrayList<>(nlChampions.values());

        // 依勝率排序分區冠軍
        Collections.sort(alChampionTeams);
        Collections.sort(nlChampionTeams);

        // 設定種子排名
        for (int i = 0; i < alChampionTeams.size(); i++) {
            alChampionTeams.get(i).seed = i + 1;
        }
        for (int i = 0; i < nlChampionTeams.size(); i++) {
            nlChampionTeams.get(i).seed = i + 1;
        }

        // 取得外卡球隊
        List<Team> alWildCards = getWildCard(alTeams, alChampionTeams);
        List<Team> nlWildCards = getWildCard(nlTeams, nlChampionTeams);



        // 顯示美聯與國聯的季後賽對戰表
        System.out.println("(AMERICAN LEAGUE)");
        System.out.println("|WILDCARD | ALDS  | ALCS    | WORLD SERIES |");
        System.out.printf("%s 6 ---\n", alWildCards.get(2).name);
        System.out.printf("%s 3 --- ? -----\n", alChampionTeams.get(2).name);
        System.out.printf("      %s 2 ----- ? -------\n", alChampionTeams.get(1).name);
        System.out.printf("%s 5 ---\n", alWildCards.get(1).name);
        System.out.printf("%s 4 --- ? -----\n", alWildCards.get(0).name);
        System.out.printf("      %s 1 ----- ? ------- ? -------\n", alChampionTeams.get(0).name);
        System.out.printf("                                       ?\n");
        System.out.printf("%s 6 --- ? ----- ? ------- ? -------\n", nlWildCards.get(2).name);
        System.out.printf("%s 3 ---\n", nlChampionTeams.get(2).name);
        System.out.printf("      %s 2 ---\n", nlChampionTeams.get(1).name);
        System.out.printf("%s 5 --- ? ----- ? -------\n", nlWildCards.get(1).name);
        System.out.printf("%s 4 ---\n", nlWildCards.get(0).name);
        System.out.printf("      %s 1 -----\n", nlChampionTeams.get(0).name);
        System.out.println("|WILDCARD | NLDS  | NLCS    | WORLD SERIES |");
        System.out.println("\n(NATIONAL LEAGUE)");
    }

    // 取得分區冠軍
    public static Map<String, Team> getChampions(List<Team> teams) {
        Map<String, Team> champions = new HashMap<>();
        for (Team team : teams) {
            champions.putIfAbsent(team.division, team);
            if (team.getWinRate() > champions.get(team.division).getWinRate()) {
                champions.put(team.division, team);
            }
        }
        return champions;
    }

    // 取得外卡球隊（非分區勝率前三高）
    public static List<Team> getWildCard(List<Team> teams, List<Team> champions) {
        List<Team> wildCards = new ArrayList<>();
        for (Team team : teams) {
            if (!champions.contains(team)) {
                wildCards.add(team);
            }
        }
        Collections.sort(wildCards);
        return wildCards.size() > 3 ? wildCards.subList(0, 3) : wildCards;
    }
}

/*一般測資
BAL AL East 101 61
BOS AL East 78 84
NYY AL East 82 80
TB AL East 99 63
TOR AL East 89 73
CWS AL Central 61 101
CLE AL Central 76 86
DET AL Central 78 84
KC AL Central 56 106
MIN AL Central 87 75
HOU AL West 90 72
LAA AL West 73 89
OAK AL West 50 112
SEA AL West 88 74
TEX AL West 90 72
ATL NL East 104 58
MIA NL East 84 78
NYM NL East 75 87
PHI NL East 90 72
WSH NL East 71 91
CHC NL Central 83 79
CIN NL Central 82 80
MIL NL Central 92 70
PIT NL Central 76 86
STL NL Central 71 91
ARI NL West 84 78
COL NL West 59 103
LAD NL West 100 62
SD NL West 82 80
SF NL West 79 83
*/


