import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Merger {

	
	public static void main(String args[]) throws IOException
    {   
		
		//
		Integer index = 1;
		Integer newIndex = 147;
		while(true){

			String infoPath = "/Users/veronica/Desktop/new44/noel.icr.exp" + index.toString() + ".info";
			File f = new File(infoPath);
			if(!f.exists()){
				break;
			}
			int count = 0;
			List<String> pictures = new ArrayList<>();
			List<String> characterList = new ArrayList<>();
			for(Integer i = index; i < index + 20; i++){
				infoPath = "/Users/veronica/Desktop/new44/noel.icr.exp" + i.toString() + ".info";
				f = new File(infoPath);
				if(!f.exists()){
					BufferedImage img1 = ImageIO.read(new File(pictures.get(0)));
					String characters1 = characterList.get(0);
					BufferedImage merged = img1;
					String mergedCharacters = characters1;
					for(int j = 1; j < pictures.size(); j++){
						BufferedImage img2 = ImageIO.read(new File(pictures.get(j)));
						String characters2 = characterList.get(j);
						merged = joinBufferedImage(img1, img2);
						mergedCharacters = characters1 + characters2;
						characters1 = mergedCharacters;
						img1 = merged;
					}
					boolean success = ImageIO.write(merged, "tif", new File("/Users/veronica/Desktop/new3/noel.icr.exp" + newIndex.toString() + ".tif"));
					writeToFile(mergedCharacters, "/Users/veronica/Desktop/new3/noel.icr.exp" + newIndex.toString() + ".info");
					
		            System.out.println("saved success? "+success);
		            return;
				}
				String characters = getCharactersFromInfoFile(infoPath);
				count += characters.length();
				if(count >= 40){
					//merge the pictures and .info
					BufferedImage img1 = ImageIO.read(new File(pictures.get(0)));
					String characters1 = characterList.get(0);
					BufferedImage merged = img1;
					String mergedCharacters = characters1;
					for(int j = 1; j < pictures.size(); j++){
						BufferedImage img2 = ImageIO.read(new File(pictures.get(j)));
						merged = joinBufferedImage(img1, img2);
						String characters2 = characterList.get(j);
						mergedCharacters = characters1 + characters2;
						characters1 = mergedCharacters;
						img1 = merged;
					}
					boolean success = ImageIO.write(merged, "tif", new File("/Users/veronica/Desktop/new3/noel.icr.exp" + newIndex.toString() + ".tif"));
					writeToFile(mergedCharacters, "/Users/veronica/Desktop/new3/noel.icr.exp" + newIndex.toString() + ".info");
		            System.out.println("saved success? "+success);
		            newIndex ++;
					index = i;
					break;
				}else{
 
					pictures.add(infoPath.substring(0, infoPath.length()-4) + "tif");
					characterList.add(characters);
				}
			}

			
			
		}
       
    }

	public static void writeToFile(String content, String path) throws IOException{
		List<String> lines = new ArrayList<>();
		lines.add(content);
		Path file = Paths.get(path);
		Files.write(file, lines, Charset.forName("UTF-8"));
	}

	public static String getCharactersFromInfoFile(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String everything = "";
		try {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();

		    while (line != null) {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        line = br.readLine();
		    }
		    everything = sb.toString();
		} finally {
		    br.close();
		}
		everything = everything.replace("\n", "").replace("\r", "");
		return everything;
	}
	
	
    public static BufferedImage joinBufferedImage(BufferedImage img1,BufferedImage img2) {

        //do some calculate first
        int offset  = 5;
        int wid = img1.getWidth()+img2.getWidth()+offset;
        int height = Math.max(img1.getHeight(),img2.getHeight())+offset;
        //create a new buffer and draw two image into the new image
        BufferedImage newImage = new BufferedImage(wid,height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = newImage.createGraphics();
        Color oldColor = g2.getColor();
        //fill background
        g2.setPaint(Color.WHITE);
        g2.fillRect(0, 0, wid, height);
        //draw image
        g2.setColor(oldColor);
        g2.drawImage(img1, null, 0, 0);
        g2.drawImage(img2, null, img1.getWidth()+offset, 0);
        g2.dispose();
        return newImage;
    }
}
