package graphics;

import javafx.scene.image.*;

import java.util.ArrayList;
import java.util.Arrays;

/** Store all pixels of a sprite. **/
public class Sprite {

    public static final int DEFAULT_SIZE = 16;
    public static final int SCALE_RATIO = 2;
    public static final int SCALED_SIZE = DEFAULT_SIZE * SCALE_RATIO;
    private static final double CORNER_RATIO = 0.3;
    public static final double CORNER_SNAP = SCALED_SIZE * CORNER_RATIO;
    private static final int TRANSPARENT_COLOR = 0xffff00ff;
    public final int SIZE;
    private final int xAxis;
    private final int yAxis;
    public int[] pixels;
    protected int realWidth;
    protected int realHeight;
    private final SpriteSheet sheet;

    public Sprite(int size, int x, int y, SpriteSheet sheet, int rw, int rh) {
        this.SIZE = size;
        this.pixels = new int[SIZE * SIZE];
        this.xAxis = x * SIZE;
        this.yAxis = y * SIZE;
        this.sheet = sheet;
        this.realWidth = rw;
        this.realHeight = rh;
        load();
    }

    public int getRealWidth() {
        return realWidth;
    }

    public int getRealHeight() {
        return realHeight;
    }

    private void load() {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                pixels[x + y * SIZE] = sheet.getPixels(x + xAxis, y + yAxis);
            }
        }
    }

    /** Get image of the sprite. **/
    public Image getFxImage() {
        WritableImage wr = new WritableImage(SIZE, SIZE);
        PixelWriter pw = wr.getPixelWriter();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if ( pixels[x + y * SIZE] == TRANSPARENT_COLOR) {
                    pw.setArgb(x, y, 0);
                }
                else {
                    pw.setArgb(x, y, pixels[x + y * SIZE]);
                }
            }
        }
        Image input = new ImageView(wr).getImage();
        return resample(input);
    }

    /** Resize source sprite into on-canvas's size **/
    private Image resample(Image input) {
        final int W = (int) input.getWidth();
        final int H = (int) input.getHeight();

        WritableImage output = new WritableImage(
                W * Sprite.SCALE_RATIO,
                H * Sprite.SCALE_RATIO
        );

        PixelReader reader = input.getPixelReader();
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                final int argb = reader.getArgb(x, y);
                for (int dy = 0; dy < Sprite.SCALE_RATIO; dy++) {
                    for (int dx = 0; dx < Sprite.SCALE_RATIO; dx++) {
                        writer.setArgb(x * Sprite.SCALE_RATIO + dx, y * Sprite.SCALE_RATIO + dy, argb);
                    }
                }
            }
        }

        return output;
    }

    /*
     *-------------------------------------------------------
     * ENTITIES' SPRITES
     *-------------------------------------------------------
     */

    /* Board objects **/
    /* ----------------------------------------------------------- **/
    public static Sprite portal
            = new Sprite(DEFAULT_SIZE, 4, 0, SpriteSheet.tiles, 14, 14);
    public static Sprite wall
            = new Sprite(DEFAULT_SIZE, 5, 0, SpriteSheet.tiles, 16, 16);
    public static Sprite grass
            = new Sprite(DEFAULT_SIZE, 6, 0, SpriteSheet.tiles, 16, 16);
    public static Sprite brick
            = new Sprite(DEFAULT_SIZE, 7, 0, SpriteSheet.tiles, 16, 16);
    /* ----------------------------------------------------------- **/

    /*
     *-------------------------------------------------------
     * BOMBS' SPRITES
     *-------------------------------------------------------
     */

    /* ----------------------------------------------------------- **/
    public static ArrayList<Sprite> bomb = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 0, 3, SpriteSheet.tiles, 15, 15),
                    new Sprite(DEFAULT_SIZE, 1, 3, SpriteSheet.tiles, 13, 15),
                    new Sprite(DEFAULT_SIZE, 2, 3, SpriteSheet.tiles, 12, 14)
            )
    );
    /* ----------------------------------------------------------- **/

    /*
     *-------------------------------------------------------
     * GAME CHARACTERS' SPRITES
     *-------------------------------------------------------
     */

    /* Bomber **/
    /* ----------------------------------------------------------- **/
    public static ArrayList<Sprite> bomber_up = new ArrayList<> (
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 0, 0, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 0, 1, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 0, 2, SpriteSheet.tiles, 12, 16)
            )
    );
    public static ArrayList<Sprite> bomber_down = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 2, 0, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 2, 1, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 2, 2, SpriteSheet.tiles, 12, 16)
            )
    );
    public static ArrayList<Sprite> bomber_left = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 3, 0, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 3, 1, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 3, 2, SpriteSheet.tiles, 12 ,16)
            )
    );

    public static ArrayList<Sprite> bomber_right = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 1, 0, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 1, 1, SpriteSheet.tiles, 12, 16),
                    new Sprite(DEFAULT_SIZE, 1, 2, SpriteSheet.tiles, 12, 16)
            )
    );

    public static ArrayList<Sprite> bomber_dead = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 4, 2, SpriteSheet.tiles, 14, 16),
                    new Sprite(DEFAULT_SIZE, 5, 2, SpriteSheet.tiles, 13, 15),
                    new Sprite(DEFAULT_SIZE, 6, 2, SpriteSheet.tiles, 16, 16)
            )
    );
    /* ----------------------------------------------------------- **/

    /* Ballom **/
    /* ----------------------------------------------------------- **/
    public static ArrayList<Sprite> ballom_left = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 9, 0, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 9, 1, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 9, 2, SpriteSheet.tiles, 16, 16)
            )
    );

    public static ArrayList<Sprite> ballom_right = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 10, 0, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 10, 1, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 10, 2, SpriteSheet.tiles, 16, 16)
            )
    );

    public static Sprite balloom_dead = new Sprite(DEFAULT_SIZE, 9, 3, SpriteSheet.tiles, 16, 16);
    /* ----------------------------------------------------------- **/

    /* Oneal **/
    /* ----------------------------------------------------------- **/
    public static ArrayList<Sprite> oneal_left = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 11, 0, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 11, 1, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 11, 2, SpriteSheet.tiles, 16, 16)
            )
    );

    public static ArrayList<Sprite> oneal_right = new ArrayList<>(
            Arrays.asList(
                    new Sprite(DEFAULT_SIZE, 12, 0, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 12, 1, SpriteSheet.tiles, 16, 16),
                    new Sprite(DEFAULT_SIZE, 12, 2, SpriteSheet.tiles, 16, 16)
            )
    );

    public static Sprite oneal_dead = new Sprite(DEFAULT_SIZE, 11, 3, SpriteSheet.tiles, 16, 16);
    /* ----------------------------------------------------------- **/

    /*
    |--------------------------------------------------------------------------
    | GameCharacter
    |--------------------------------------------------------------------------

    //Dall
    public static Sprite doll_left1 = new Sprite(DEFAULT_SIZE, 13, 0, SpriteSheet.tiles, 16, 16);
    public static Sprite doll_left2 = new Sprite(DEFAULT_SIZE, 13, 1, SpriteSheet.tiles, 16, 16);
    public static Sprite doll_left3 = new Sprite(DEFAULT_SIZE, 13, 2, SpriteSheet.tiles, 16, 16);

    public static Sprite doll_right1 = new Sprite(DEFAULT_SIZE, 14, 0, SpriteSheet.tiles, 16, 16);
    public static Sprite doll_right2 = new Sprite(DEFAULT_SIZE, 14, 1, SpriteSheet.tiles, 16, 16);
    public static Sprite doll_right3 = new Sprite(DEFAULT_SIZE, 14, 2, SpriteSheet.tiles, 16, 16);

    public static Sprite doll_dead = new Sprite(DEFAULT_SIZE, 13, 3, SpriteSheet.tiles, 16, 16);

    //Minvo
    public static Sprite minvo_left1 = new Sprite(DEFAULT_SIZE, 8, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite minvo_left2 = new Sprite(DEFAULT_SIZE, 8, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite minvo_left3 = new Sprite(DEFAULT_SIZE, 8, 7, SpriteSheet.tiles, 16, 16);

    public static Sprite minvo_right1 = new Sprite(DEFAULT_SIZE, 9, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite minvo_right2 = new Sprite(DEFAULT_SIZE, 9, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite minvo_right3 = new Sprite(DEFAULT_SIZE, 9, 7, SpriteSheet.tiles, 16, 16);

    public static Sprite minvo_dead = new Sprite(DEFAULT_SIZE, 8, 8, SpriteSheet.tiles, 16, 16);

    //Kondoria
    public static Sprite kondoria_left1 = new Sprite(DEFAULT_SIZE, 10, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite kondoria_left2 = new Sprite(DEFAULT_SIZE, 10, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite kondoria_left3 = new Sprite(DEFAULT_SIZE, 10, 7, SpriteSheet.tiles, 16, 16);

    public static Sprite kondoria_right1 = new Sprite(DEFAULT_SIZE, 11, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite kondoria_right2 = new Sprite(DEFAULT_SIZE, 11, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite kondoria_right3 = new Sprite(DEFAULT_SIZE, 11, 7, SpriteSheet.tiles, 16, 16);

    public static Sprite kondoria_dead = new Sprite(DEFAULT_SIZE, 10, 8, SpriteSheet.tiles, 16, 16);

    //ALL
    public static Sprite mob_dead1 = new Sprite(DEFAULT_SIZE, 15, 0, SpriteSheet.tiles, 16, 16);
    public static Sprite mob_dead2 = new Sprite(DEFAULT_SIZE, 15, 1, SpriteSheet.tiles, 16, 16);
    public static Sprite mob_dead3 = new Sprite(DEFAULT_SIZE, 15, 2, SpriteSheet.tiles, 16, 16);

    |--------------------------------------------------------------------------
    | FlameSegment Sprites
    |--------------------------------------------------------------------------
    */
    public static Sprite bomb_exploded = new Sprite(DEFAULT_SIZE, 0, 4, SpriteSheet.tiles, 16, 16);
    public static Sprite bomb_exploded1 = new Sprite(DEFAULT_SIZE, 0, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite bomb_exploded2 = new Sprite(DEFAULT_SIZE, 0, 6, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_vertical = new Sprite(DEFAULT_SIZE, 1, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical1 = new Sprite(DEFAULT_SIZE, 2, 5, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical2 = new Sprite(DEFAULT_SIZE, 3, 5, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_horizontal = new Sprite(DEFAULT_SIZE, 1, 7, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal1 = new Sprite(DEFAULT_SIZE, 1, 8, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal2 = new Sprite(DEFAULT_SIZE, 1, 9, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_horizontal_left_last = new Sprite(DEFAULT_SIZE, 0, 7, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal_left_last1 = new Sprite(DEFAULT_SIZE, 0, 8, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal_left_last2 = new Sprite(DEFAULT_SIZE, 0, 9, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_horizontal_right_last = new Sprite(DEFAULT_SIZE, 2, 7, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal_right_last1 = new Sprite(DEFAULT_SIZE, 2, 8, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_horizontal_right_last2 = new Sprite(DEFAULT_SIZE, 2, 9, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_vertical_top_last = new Sprite(DEFAULT_SIZE, 1, 4, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical_top_last1 = new Sprite(DEFAULT_SIZE, 2, 4, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical_top_last2 = new Sprite(DEFAULT_SIZE, 3, 4, SpriteSheet.tiles, 16, 16);

    public static Sprite explosion_vertical_down_last = new Sprite(DEFAULT_SIZE, 1, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical_down_last1 = new Sprite(DEFAULT_SIZE, 2, 6, SpriteSheet.tiles, 16, 16);
    public static Sprite explosion_vertical_down_last2 = new Sprite(DEFAULT_SIZE, 3, 6, SpriteSheet.tiles, 16, 16);

    /*
    |--------------------------------------------------------------------------
    | Brick FlameSegment
    |--------------------------------------------------------------------------
    */
    public static Sprite brick_exploded = new Sprite(DEFAULT_SIZE, 7, 1, SpriteSheet.tiles, 16, 16);
    public static Sprite brick_exploded1 = new Sprite(DEFAULT_SIZE, 7, 2, SpriteSheet.tiles, 16, 16);
    public static Sprite brick_exploded2 = new Sprite(DEFAULT_SIZE, 7, 3, SpriteSheet.tiles, 16, 16);
    /*
    |--------------------------------------------------------------------------
    | Powerups
    |--------------------------------------------------------------------------
    public static Sprite powerup_bombs = new Sprite(DEFAULT_SIZE, 0, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_flames = new Sprite(DEFAULT_SIZE, 1, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_speed = new Sprite(DEFAULT_SIZE, 2, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_wallpass = new Sprite(DEFAULT_SIZE, 3, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_detonator = new Sprite(DEFAULT_SIZE, 4, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_bombpass = new Sprite(DEFAULT_SIZE, 5, 10, SpriteSheet.tiles, 16, 16);
    public static Sprite powerup_flamepass = new Sprite(DEFAULT_SIZE, 6, 10, SpriteSheet.tiles, 16, 16);

    */
}
