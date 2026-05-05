import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MultiThresholdSegmentation {
    public static void main(String[] args) {
        try {
            // 1. 讀取原始圖片 (對應你照片左邊的 001.jpg)
            File inputFile = new File("444.png"); 
            
            if (!inputFile.exists()) {
                System.out.println("找不到 444.png，請確認圖片有放在資料夾裡喔！");
                return;
            }
            
            BufferedImage inputImage = ImageIO.read(inputFile);
            int width = inputImage.getWidth();
            int height = inputImage.getHeight();
            
            // 建立黑白輸出圖片
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            
            // 2. 設定 HSV 的多重閾值 (Multi-threshold)
            // 在 Java 中，Hue (色相) 的範圍是 0.0 ~ 1.0
            // 橘色/黃色 (狗狗的顏色) 的 Hue 大約落在 0.02 ~ 0.18 之間
            // 藍色/灰色 (背景) 會大於 0.5
            float minHue = 0.0f;   // 閾值 1 (紅色端)
            float maxHue = 0.18f;  // 閾值 2 (黃綠色端)
            
            // 加上飽和度閾值，過濾掉太灰暗沒有顏色的雜訊
            float minSaturation = 0.15f; 

            // 3. 雙重迴圈：遍歷所有像素
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = new Color(inputImage.getRGB(x, y));
                    
                    // 【核心魔法】將 RGB 轉換為 HSB (HSV)
                    float[] hsbVals = new float[3];
                    Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbVals);
                    
                    float hue = hsbVals[0];         // 色相 (顏色)
                    float saturation = hsbVals[1];  // 飽和度 (鮮豔程度)
                    // float brightness = hsbVals[2]; // 亮度 (這次我們不需要看亮度)
                    
                    int newPixelValue;
                    
                    // 4. 多重閾值判斷：只保留「顏色是橘黃色」且「有一定鮮豔度」的像素
                    if (hue >= minHue && hue <= maxHue && saturation >= minSaturation) {
                        // 是狗狗的顏色！設為純白
                        newPixelValue = 255;
                    } else {
                        // 其他顏色 (藍色背景、黑色眼睛鼻子)，設為純黑
                        newPixelValue = 0;
                    }
                    
                    Color newColor = new Color(newPixelValue, newPixelValue, newPixelValue);
                    outputImage.setRGB(x, y, newColor.getRGB());
                }
            }
            
            // 5. 輸出檔案 (對應你照片右邊的 hsv_001.png)
            File outputFile = new File("hsv_001.png");
            ImageIO.write(outputImage, "png", outputFile);
            System.out.println("大功告成！結果已儲存為 hsv_001.png");
            
        } catch (Exception e) {
            System.out.println("發生錯誤：" + e.getMessage());
        }
    }
}
