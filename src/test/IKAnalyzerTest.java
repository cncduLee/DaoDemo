package test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

/**
 * @Intro descrption here
 * @author Lee
 * @Date 2013-8-22
 */
public class IKAnalyzerTest {
	private final static Log log = LogFactory.getLog(IKAnalyzerTest.class);

//    private final static IKAnalyzer analyzer = new IKAnalyzer();
//    private final static List<String> nowords = new ArrayList<String>(){{//可用可不用
//        try{
//            addAll(IOUtils.readLines(IKAnalyzerTest.class.getResourceAsStream("/ext.dic")));
//        }catch(IOException e){
//            log.error("Unabled to read stopword file", e);
//        }
//    }};
	
    /**
     * 关键字切分
     * @param sentence 要分词的句子
     * @return 返回分词结果
     */
    public static List<String> splitKeywords(String sentence) {

        List<String> keys = new ArrayList<String>();

        if(!StringUtils.isBlank(sentence))  {
            StringReader reader = new StringReader(sentence);
            IKSegmentation ikseg = new IKSegmentation(reader, true);
            try{
                do{
                    Lexeme me = ikseg.next();
                    if(me == null)
                        break;
                    String term = me.getLexemeText();
                    
                    if(StringUtils.isNumeric(StringUtils.remove(term,'.')))
                        continue;
//                    if(nowords.contains(term.toLowerCase()))
//                        continue;
                    
                    keys.add(term);
                    
                }while(true);
            }catch(IOException e){
                log.error("Unable to split keywords", e);
            }
        }

        return keys;
    }
    
    
	public static void main(String[] args) {
		String text = "选择可以体现您的情绪或个性的主题模板！还可以按您自己的想法设置首页排版方式。";
		long ct = System.currentTimeMillis();
		
		for(String word : IKAnalyzerTest.splitKeywords(text)){
			System.out.print(word+"|");
		}
		
		System.out.printf("\nTIME %d ms", (System.currentTimeMillis() - ct) );
	}
}
