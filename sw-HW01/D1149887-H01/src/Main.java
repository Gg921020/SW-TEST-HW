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
    public int compareTo(Team team) {
        return Double.compare(team.getWinRate(), this.getWinRate());
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
                    throw new IllegalArgumentException("輸入異常. Expected: TeamName League Division Wins Losses");
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
                    throw new IllegalArgumentException("聯盟名稱輸入異常. 應該是 AL or NL.");
                }
                if(totalGames != 162){
                    throw new IllegalArgumentException("場次輸入異常. 總場次應該是162.");
                }
            } catch (Exception e) {
                System.out.println("Error processing input: " + e.getMessage());
                i--; // 重新輸入該隊資訊
            }
        }




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

*/


