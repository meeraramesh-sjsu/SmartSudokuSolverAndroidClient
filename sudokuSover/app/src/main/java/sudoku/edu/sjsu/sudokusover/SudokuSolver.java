package sudoku.edu.sjsu.sudokusover;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SudokuSolver {

    private int set(int x, int number) {
        return x | (1 << (9 - number));
    }

    private int clear(int x, int number) {
        int y = 1 << (9 - number);
        return x & ~y;
    }

    private int getNum() {
        return (1 << 9) - 1;
    }

    private int getCubeIdx(int i, int j) {

        int idiv = i / 3;
        int jdiv = j / 3;
        if (idiv == 0) {
            return jdiv == 0 ? 0 : jdiv == 1 ? 1 : 2;
        }
        if (idiv == 1) {
            return jdiv == 0 ? 3 : jdiv == 1 ? 4 : 5;
        }
        return jdiv == 0 ? 6 : jdiv == 1 ? 7 : 8;
    }

    private int[] getCubeStart(int i, int j) {
        int[] result = new int[2];
        result[0] = result[1] = -1;
        int irem = i % 3;
        int jrem = j % 3;
        result[0] = i - irem;
        result[1] = j - jrem;
        return result;
    }

    private List<Integer> getValidNums(int x) {
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < 9; i++) {
            if ((x & (1 << i)) > 0) {
                list.add(9 - i);
            }
        }
        return list;
    }

    private boolean isLegal(int[][] game, int ci, int cj) {
        boolean[] tst = new boolean[10];
        for (int i = 0; i < 9; i++) {
            if (game[i][cj] > 0) {
                if (tst[game[i][cj]]) {
                    return false;
                }
                tst[game[i][cj]] = true;
            }
        }

        tst = new boolean[10];
        for (int j = 0; j < 9; j++) {
            if (game[ci][j] > 0) {
                if (tst[game[ci][j]]) {
                    return false;
                }
                tst[game[ci][j]] = true;
            }
        }

        int[] res = getCubeStart(ci, cj);
        if (res[0] == -1 || res[1] == -1) {
            return false;
        }

        tst = new boolean[10];
        for (int i = res[0]; i < res[0] + 3; i++) {
            for (int j = res[1]; j < res[1] + 3; j++) {
                if (game[i][j] > 0) {
                    if (tst[game[i][j]]) {
                        return false;
                    }
                    tst[game[i][j]] = true;
                }
            }
        }

        return true;
    }

    public void solveSudoku(char[][] board) {
        if (board == null || board.length == 0) {
            return;
        }
        int[] horiz, vert, cube;
        horiz = new int[9];
        vert = new int[9];
        cube = new int[9];

        for (int i = 0; i < 9; i++) {
            int value = getNum();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.') {
                    int entry = board[i][j] - '0';
                    value = clear(value, entry);
                }
            }
            horiz[i] = value;
        }

        for (int j = 0; j < 9; j++) {
            int value = getNum();
            for (int i = 0; i < 9; i++) {
                if (board[i][j] != '.') {
                    int entry = board[i][j] - '0';
                    value = clear(value, entry);
                }
            }
            vert[j] = value;
        }

        int p = 0;
        for (int h = 0; h < 9; h += 3) {
            for (int i = 0; i < 9; i += 3) {
                int value = getNum();
                for (int j = h; j < h + 3; j++) {
                    for (int k = i; k < i + 3; k++) {
                        if (board[j][k] != '.') {
                            int entry = board[j][k] - '0';
                            value = clear(value, entry);
                        }
                    }
                }
                cube[p++] = value;
            }
        }

        Map<String, List<Integer>> map = new HashMap<>();
        int[][] game = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    int poss = horiz[i] & vert[j] & cube[getCubeIdx(i, j)];
                    map.put(i + "-" + j, getValidNums(poss));
                } else {
                    game[i][j] = board[i][j] - '0';
                }
            }
        }

        playgame(map, game, 0, 0);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == '.') {
                    char c = (char) ('0' + game[i][j]);
                    board[i][j] = c;
                }
            }
        }
    }

    private boolean playgame(Map<String, List<Integer>> map, int[][] game, int ci, int cj) {
        if (ci == 8 && cj == 9) {
            return true;
        }
        if (cj == 9) {
            return playgame(map, game, ci + 1, 0);
        }
        if (game[ci][cj] > 0) {
            return playgame(map, game, ci, cj + 1);
        }
        for (int pVal : map.get(ci + "-" + cj)) {
            game[ci][cj] = pVal;
            if (isLegal(game, ci, cj)) {
                if (playgame(map, game, ci, cj + 1)) {
                    return true;
                }
            }
        }
        game[ci][cj] = 0;
        return false;
    }

}