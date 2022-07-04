import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 无用
 *
 */

public class BMP {
	File file;
	
	/* 头部十进制表 */
	String Head[] = new String[54];
	
	/* 位图文件头 */
	public String bfType; //2Bytes，必须为"BM"，即0x4D42 才是Windows位图文件
	public long bfSize; //4Bytes，整个BMP文件的大小
	public int bfReserved1; //2Bytes，保留，为0
	public int bfReserved2; //2Bytes，保留，为0
	public int bfOffBits; //4Bytes，文件起始位置到图像像素数据的字节偏移量
	
	/* 位图信息头 */
	public int biSize; //4Bytes,位图信息头所占字节（固定值）
	public int biWidth; //4Bytes，图像宽度（以像素为单位）
	public int biHeight; //4Bytes，图像高度（以像素为单位） + 图片存储顺序,正数为倒序
	public int biPlanes; //2Bytes，图像数据平面，BMP存储RGB数据，因此总为1
	public int biBitCount; //2Bytes，图像像素位数
	public int biCompression; //4Bytes，0：不压缩，1：RLE8，2：RLE4
	public int biSizeImage; //4Bytes，4字节对齐的图像数据大小
	public int biXPelsPerMeter; //4Bytes，用象素/米表示的水平分辨率
	public int biYPelsPerMeter; //4Bytes，用象素/米表示的垂直分辨率
	public int biClrUsed; //4Bytes，实际使用的调色板索引数，0：使用所有的调色板索引
	public int biClrImportant; //4Bytes，重要的调色板索引数，0：所有的调色板索引都重要
	
	public void init(File img_file) throws IOException {
		this.file = img_file;
		InputStream img_stream = new FileInputStream(img_file);
		int size = img_stream.available();//读取到的数据个数
		System.out.println("读取到的数据个数: "+size);
		size = size>54? 54:size;
		for(int i = 0; i < size; i++) {
			Head[i] = Integer.toHexString(img_stream.read());
		}
		
		bfType = Head[1]+Head[0];
		bfSize = Integer.valueOf(Head[5]+Head[4]+Head[3]+Head[2],16);
		bfReserved1 = Integer.valueOf(Head[7]+Head[6],16);
		bfReserved2 = Integer.valueOf(Head[9]+Head[8],16);
		bfOffBits = Integer.valueOf(Head[13]+Head[12]+Head[11]+Head[10],16);
		
		biSize = Integer.valueOf(Head[17]+Head[16]+Head[15]+Head[14],16);
		biWidth = Integer.valueOf(Head[21]+Head[20]+Head[19]+Head[18],16);
		biHeight = Integer.valueOf(Head[25]+Head[24]+Head[23]+Head[22],16);
		biPlanes = Integer.valueOf(Head[27]+Head[26],16);
		biBitCount = Integer.valueOf(Head[29]+Head[28],16);
		biCompression = Integer.valueOf(Head[33]+Head[32]+Head[31]+Head[30],16);
		biSizeImage =  Integer.valueOf(Head[37]+Head[36]+Head[35]+Head[34],16);
		biXPelsPerMeter = Integer.valueOf(Head[41]+Head[40]+Head[39]+Head[38],16);
		biYPelsPerMeter = Integer.valueOf(Head[45]+Head[44]+Head[43]+Head[42],16);
		biClrUsed = Integer.valueOf(Head[49]+Head[48]+Head[47]+Head[46],16);
		biClrImportant = Integer.valueOf(Head[53]+Head[52]+Head[51]+Head[50],16);
		
		img_stream.close();
	}
	
	public void printBMPinfo() {
		System.out.println("文件类型: "+bfType);
		System.out.println("文件大小: "+bfSize+" 字节");
		System.out.println("保留区域1: "+bfReserved1);
		System.out.println("保留区域2: "+bfReserved2);
		System.out.println("像素偏移: "+bfOffBits);
		System.out.println("信息头大小: "+biSize);
		System.out.println("图像宽度: "+biWidth+" 像素");
		System.out.println("图像高度: "+biHeight+" 像素");
		System.out.println("数据平面: "+biPlanes);
		System.out.println("像素位数: "+biBitCount);
		System.out.println("压缩类型: "+biCompression);
		System.out.println("图像数据大小: "+biSizeImage);
		System.out.println("水平分辨率: "+biXPelsPerMeter);
		System.out.println("垂直分辨率: "+biYPelsPerMeter);
		System.out.println("实际调色板索引数: "+biClrUsed);
		System.out.println("重要调色板索引数: "+biClrImportant);
	}
	
	public int getStartAddressInMiddle() {//事实上,int已经足够了
		int start_address = 0;
		start_address = (int) (bfSize - (biWidth * 3 * biHeight * 0.5));
		System.out.println("图片开始点:"+start_address);
		return start_address;
	}
	
	public void getBlockbyWhite(File img_file) throws IOException {
		/** 通过1080*3个连续的FF进行识别
		 * 结果:
		 * 1 : 发现1段全白行,位于:	158646	26bb6
		 * 2 : 发现1段全白行,位于:	314262	4cb96
		 * 3 : 发现1段全白行,位于:	933846	e3fd6
		 * 4 : 发现1段全白行,位于:	1195302	123d26
		 * 5 : 发现1段全白行,位于:	1558078	17c63e
		 * 6 : 发现1段全白行,位于:	2231958	220e96
		 * 7 : 发现1段全白行,位于:	2854037	2b8c95
		 * 8 : 发现1段全白行,位于:	3476160	350ac0
		 * 9 : 发现1段全白行,位于:	3735414	38ff76
		 * 10 : 发现1段全白行,位于:	4098244	3e88c4
		 * 11 : 发现1段全白行,位于:	4772118	48d116
		 * 12 : 发现1段全白行,位于:	5394198	524f16
		 * 13 : 发现1段全白行,位于:	6094362	5cfe1a
		 * 其中12和13是正确的
		 */
		InputStream stream_file = new FileInputStream(img_file);
		int recordTime = 0;
		int recordTime_sum = 0;
		int lastgetNum = 0;
		int getNum = 0;
		boolean echo = true;
		int num = stream_file.available();
		for(int i=0; i<num; i++) {
			
			lastgetNum = getNum;
			getNum = stream_file.read();
			
			if(getNum == 255 && lastgetNum == 255 ) {
				recordTime++;
			}else {
				recordTime = 0;
				echo = true;
			}
			
			if(recordTime >= 1080*3 && echo) {
				recordTime_sum++;
				System.out.println(recordTime_sum+" : 发现1段全白行,位于:\t"+i+"\t"+Integer.toHexString(i));
				recordTime = 0;
				echo = false;
			}
			
		}
		stream_file.close();
	}
	
	public void getBlockbyEquidistantColorv1(File img_file) throws IOException {
		// 等距取样每行颜色信息,做比率
		System.out.println("开始读取文件");
		BufferedInputStream read_bmp = new BufferedInputStream(new FileInputStream(img_file));
		System.out.println("开始消耗无用内容");
		int StartAddressInMiddle = getStartAddressInMiddle();
		read_bmp.read(new byte[StartAddressInMiddle]);// 消耗掉无用的内容
		System.out.println("开始读取图片每一行");
		byte[] bmp_line_byte = new byte[(int)biWidth * 3];//这是图片的每一行
		int len = 0;//读取到的数据长度,应当等于图片宽度*3
		byte[] equidistant_list;//等距取样后的数组
		byte[] equidistant_list_middle = new byte[3];//等距取样后的数组的中间颜色
		int same_times = 0;//遍历等距取样后数组时,前后两个颜色一样的次数
		int e_l_mnum;//等距取样后数组中间值
//		while( (len = read_bmp.read(bmp_line_byte)) != -1) {//循环读取图片每一行
//			System.out.println("len的长度:" + len);	
//		}
		int startwhere = (StartAddressInMiddle - bfOffBits) / (biWidth * 3);//最开始扫描位置不是图片开头,虽然现在的结果是1200,提高兼容性,仍然要算一下
		
		int temp1=0;
		while( (len = read_bmp.read(bmp_line_byte)) != -1) {
			equidistant_list = Equidistant_sampling(bmp_line_byte, (int)(len*0.2), (int)(len*0.8), 3, 30);
			e_l_mnum = equidistant_list.length / 2;
			equidistant_list_middle[0] = equidistant_list[e_l_mnum]; 
			equidistant_list_middle[1] = equidistant_list[e_l_mnum+1]; 
			equidistant_list_middle[2] = equidistant_list[e_l_mnum+2]; 
			for(int i = 0; i < equidistant_list.length; i+=3) {//遍历该行颜色
				if(equidistant_list[i]*equidistant_list[i+1]*equidistant_list[i+2] == equidistant_list_middle[0]*equidistant_list_middle[1]*equidistant_list_middle[2]) {
					same_times++;
					{
						temp1++;
						System.out.println("发现相同颜色:\t"+same_times+"\t"+temp1);
					}
				}
			}
			if(same_times >= (int) (0.7 * equidistant_list.length)) {
				System.out.println("发现同颜色横线条");
			}
			same_times = 0;
		}
		read_bmp.close();
	}
	
	public void getBlockbyEquidistantColorv2(File img_file) throws IOException {
		//v2 比较每行连续相同颜色占总行的比例范围
		System.out.println("开始读取文件");
		BufferedInputStream read_bmp = new BufferedInputStream(new FileInputStream(img_file));
		System.out.println("开始消耗无用内容");
		int StartAddressInMiddle = getStartAddressInMiddle();
		read_bmp.read(new byte[StartAddressInMiddle]);// 消耗掉无用的内容
		System.out.println("开始读取图片每一行");
		byte[] bmp_line_byte = new byte[(int)biWidth * 3];//这是图片的每一行
		int spendline = 0;//扫描过的行数
		int current_address = 0;
		int len = 0;//读取到的数据长度,应当等于图片宽度*3
		byte[] last_char = {0,0,0};
		int thesametime = 0;
		int time = 0;
		int startwhere = (StartAddressInMiddle - bfOffBits) / (biWidth * 3);//最开始扫描位置不是图片开头,虽然现在的结果是1200,提高兼容性,仍然要算一下
		
		while( (len = read_bmp.read(bmp_line_byte)) != -1) {
			spendline++;
			thesametime = 0;
			for(int i = 0; i < bmp_line_byte.length; i+=3) {
				if(bmp_line_byte[i]==last_char[0] && bmp_line_byte[i+1]==last_char[1] && bmp_line_byte[i+2]==last_char[2] && bmp_line_byte[i]+bmp_line_byte[i+1]+bmp_line_byte[i+2]!=0 && bmp_line_byte[i]+bmp_line_byte[i+1]+bmp_line_byte[i+2]!=765) {
					thesametime++;
				}else{
					if(thesametime>=(int)(biWidth*0.8)&&thesametime<(int)(biWidth*0.9)) {
						time++;
						current_address = spendline+startwhere;
						System.out.println( bmp_line_byte[i]+bmp_line_byte[i+1]+bmp_line_byte[i+2]);
						System.out.println("发现可能的行: "+ time +" 次，在 "+ current_address +" 行,位置为: "+ Integer.toHexString((current_address-1)*1080*3+bfOffBits) +" 到 "+ Integer.toHexString(current_address*1080*3+bfOffBits));
					}
					thesametime = 0;
				}
				last_char[0] = bmp_line_byte[i];
				last_char[1] = bmp_line_byte[i+1];
				last_char[2] = bmp_line_byte[i+2];
			}
		}
		read_bmp.close();
	}
	
	public void getBlockbyEquidistantColor_test(File img_file) throws IOException {
		//v3 黑白化扫描到的颜色，再识别
		System.out.println("开始读取文件");
		BufferedInputStream read_bmp = new BufferedInputStream(new FileInputStream(img_file));
		System.out.println("开始消耗无用内容");
		int StartAddressInMiddle = getStartAddressInMiddle();
		read_bmp.read(new byte[StartAddressInMiddle]);// 消耗掉无用的内容
		System.out.println("开始读取图片每一行");
		byte[] bmp_line_byte = new byte[biWidth * 3];//这是图片的每一行
		int spendline = 0;//扫描过的行数
		int current_address = 0; //当前行数在文件中的十六进制坐标
		int len = 0;//读取到的数据长度,应当等于图片宽度*3
		int startwhere = (StartAddressInMiddle - bfOffBits) / (biWidth * 3);//最开始扫描位置不是图片开头,虽然现在的结果是1200,提高兼容性,仍然要算一下
		byte[] bwColor = new byte[biWidth];//把扫描到的数据黑白化
		int wColor = 0, bColor = 0;//bwColor中0的个数和1的个数
		
		
		//发现可能值的次数
		int findtime = 0;
		//ff存到byte里会溢出
		while( (len = read_bmp.read(bmp_line_byte)) != -1) {
			for(int i = 0; i < len; i += 3) {
				System.out.println(bmp_line_byte[i]+bmp_line_byte[i+1]+bmp_line_byte[i+2]);
				if(bmp_line_byte[i]+bmp_line_byte[i+1]+bmp_line_byte[i+2]==765) {//rgb(ff,ff,ff)转为0，其他为1 
					bwColor[i/3] = 1;
					wColor++;
				}else {
					bwColor[i/3] = 2;
					bColor++;
				}
			}
			if(bColor >= (int)(biWidth*0.8)) {
				findtime++;
//				System.out.print("发现了可能的值 "+findtime+"次 ");
//				for(byte j : bwColor) {
//					System.out.print(j);
//				}
//				System.out.println();
			}
		}
	}
	
	private byte[] Equidistant_sampling(byte[] sourcelist ,int start, int end, int block, int num) {//原始数组,开始位置,结束位置,每n个看作一个整体,要获取的数量
		/* 对存放rgb的数组进行等距取样 */
		
		//处理一下输入的值
		if(sourcelist == null || sourcelist.length == 0 ) return null;
		if(start < 1) start = 1;
		if(end > sourcelist.length) end = sourcelist.length;
		if(block > end - start + 1 || block < 1) block = end - start + 1;
		if(num > (end - start + 1)/block) num = (end - start + 1)/block;
		
		byte[] endlist = new byte[num*block];
		for(int i = 0; i < num; i++) {
			for(int j = 0; j < block; j++) {
//				endlist[i+j] = 
//				o oo oo oo oo oo o
//				start = 2 ; end = lenght - 1 =11 ; block = 2 ; num = 2
//				选中的总长度 l = end - start + 1  = 11-2+1 = 10
//				总长度分成几个小整体 N = l / block = 10/2 = 5
//				小整体等距取样 jj = N/num = 5/2 = 2
//				取小整体里的第一个,算出start到end里应取的具体位置 w = (jj*block) * i
//				在整个list中的每段第一个取值的位置: where = w + start-1	
//				合并 
				endlist[i*block+j] = (byte) (((end-start+1)/num )*i + start - 1);
			}
		}
		return endlist;
	}
	
	
	
}