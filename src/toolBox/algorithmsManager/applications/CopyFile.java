package toolBox.algorithmsManager.applications;

import java.io.*;

public class CopyFile {
	public CopyFile(){
	}
	public void copyAToB(String src,String dst){
			File in=new File(src);
			File out=new File(dst);
			if(!in.exists()){
				System.out.println(in.getAbsolutePath()+"源文件路径错误！！！");
				return;
			}
			if(!out.exists()) 
				out.mkdirs();
			FileInputStream fin=null;
			FileOutputStream fout=null;
			if(in.isFile()){
				try {
					fin=new FileInputStream(in);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("in.name="+in.getName());
				try {
					fout=new FileOutputStream(new File(dst+"/"+in.getName()));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int c;
				byte[] b=new byte[1024*5];
				try {
					while((c=fin.read(b))!=-1){					
						fout.write(b, 0, c);
					}
					fin.close();
					fout.flush();
					fout.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else copyAToB(src+"/"+in.getName(),dst+"/"+in.getName());
			}

	

}
