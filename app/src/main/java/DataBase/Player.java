package DataBase;

public class Player {
        private int id;
        private String name;
        private int score;
        private boolean hasRegainedPoint;

    public Player(int id, String name, int score, boolean hasRegainedPoint) {
            this.id = id;
            this.name = name;
            this.score = score;
            this.hasRegainedPoint = hasRegainedPoint;
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

        public boolean hasRegainedPoint() {
            return hasRegainedPoint;
        }

        public void setHasRegainedPoint(boolean hasRegainedPoint) {
            this.hasRegainedPoint = false;
        }
}
