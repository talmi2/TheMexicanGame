package DataBase;

public class Player {
        private int id;
        private String name;
        private int score;

        public Player(int id, String name, int score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public int getScore() {
            return this.score;
        }
}
