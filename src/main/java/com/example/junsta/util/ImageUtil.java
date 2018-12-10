package com.example.junsta.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

public class ImageUtil {
	private static final int IMG_WIDTH = 540;     // 이미지 width 상황에 맞게 설정
	  private static final int IMG_HEIGHT = 700;    // 이미지 height 상황에 맞게 설정
	 
	  /**
	   * 원본 이미지를 리사이즈 한다.
	   *
	   * @param String srcPath 원본 이미지 경로
	   * @param String destPath 대상 경로
	   * @param String fileName 파일명
	   * @param String suffix 확장자 ( .jpg , .png )
	   * @throws IOException
	   */
	  public static void imageResize(String srcPath, String destPath, String fileName, String suffix)
	      throws IOException {
	 
	    File destFolder = new File(destPath);
	 
	    if (!destFolder.exists()) {
	      destFolder.mkdirs();
	    }
	 
	    // File file = new File(srcPath + fileName + suffix);
	    BufferedImage originalImage = ImageIO.read(new File(srcPath + fileName + "." + suffix));
	    String convertUUID = UUID.randomUUID().toString().replace("-", ""); // 해쉬명 생성
	 
	    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	    BufferedImage resizeImage = resizeImageWithHint(originalImage, type);
	 
	    ImageIO.write(resizeImage, suffix.substring(1), new File(destPath + convertUUID + suffix));
	  }
	 
	  /**
	   * 이미지를 리사이즈 한다.
	   *
	   * @param File srcFile 원본 이미지 파일
	   * @param String destPath 대상 경로
	   * @param String hashfileName 해쉬파일명
	   * @param String suffix 확장자 ( .jpg , .png )
	   * @throws IOException
	   */
	  public static void imageResize(File srcFile, String destPath, String hashfileName, String suffix)
	      throws IOException {
	 
	    File destFolder = new File(destPath);
	 
	    if (!destFolder.exists()) {
	      destFolder.mkdirs();
	    }
	 
	    BufferedImage originalImage = ImageIO.read(srcFile);
	    int type = originalImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	    BufferedImage resizeImage = resizeImageWithHint(originalImage, type);
	 
	    ImageIO.write(resizeImage, suffix.substring(1), new File(destPath + hashfileName + suffix));
	  }
	 
	 
	  private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
	 
	    BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
	    Graphics2D g = resizedImage.createGraphics();
	    g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
	    g.dispose();
	    g.setComposite(AlphaComposite.Src);
	 
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	 
	    return resizedImage;
	  }


}
