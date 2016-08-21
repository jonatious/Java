import java.awt.image.Raster;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class ConvertImageToSingleDimension {
    public static void main(String[] args) {
        ArrayList<Float> imageValues = new ArrayList<Float>();
        String path = "C:\\Users\\Karthikk\\Pictures\\ganesh-vtv.jpg";
        ImageReader imageReader = new ImageReader();
        HilbertCurveGenerator hilbertUtil = new HilbertCurveGenerator();

        Raster imageRaster = imageReader.getRasterDataForImage(imageReader.readImage(path));
        int boundValue = imageReader.getImageBounds(imageRaster);
        Hashtable paddingValues = imageReader.getXAndYPaddingValues(imageRaster);

        for (int i = 0; i < boundValue*boundValue; i++) {
            ArrayList<Integer> pixelValue = hilbertUtil.d2xy(boundValue,i);
            imageValues.add(imageReader.checkAndReturnPixelValue(pixelValue.get(0),pixelValue.get(1),imageRaster,paddingValues));
        }

//  Use Image values to write to the required values
   }
}
