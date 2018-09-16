package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontUtils {
    final static FontUtils instance = new FontUtils();

    public FontUtils(){

    }

    /**
     *
     * @param size font size
     * @param xOffset x offset for shadow
     * @param yOffset y offset for shadow
     * @param border font border
     * @param color font color
     * @param shadow color of shadow
     * @param borderColor color of border
     * @param url location of font file in files system
     * @return returns the entire generator..,
     */
    public static BitmapFont generateFont(int size, int xOffset, int yOffset, int border, Color color, Color shadow, Color borderColor, Color textColor, String url){
        final FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(url));
        final FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = size;
        parameter.borderColor = color;
        parameter.borderWidth = border;
        parameter.shadowOffsetX = xOffset;
        parameter.shadowOffsetY = yOffset;
        parameter.shadowColor = shadow;
        parameter.borderColor = borderColor;
        parameter.color = textColor;
        parameter.genMipMaps = true;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

}
