package fr.delcey.mexicangame;

public enum ModelObject {
    GREEN(R.color.mexico_green, R.layout.goal),
    WHITE(R.color.white, R.layout.the_game),
    RED(R.color.mexico_red, R.layout.the_game_),
    GREEN2(R.color.mexico_green, R.layout.scoring),
    WHITE2(R.color.white, R.layout.special_scoring),
    RED2(R.color.mexico_red, R.layout.game_order),
    GREEN3(R.color.mexico_green, R.layout.endgame),
    WHITE3(R.color.white, R.layout.endgame_),
    RED3(R.color.mexico_red, R.layout.lets_play);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
