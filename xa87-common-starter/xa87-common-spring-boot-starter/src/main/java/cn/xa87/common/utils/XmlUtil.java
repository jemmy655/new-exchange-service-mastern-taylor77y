package cn.xa87.common.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出xml和解析xml的工具类
 *
 * @ClassName:XmlUtil
 * @Description:
 */
@Slf4j
public class XmlUtil {
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int index = 0; index < nodeList.getLength(); ++index) {
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
            }
            return data;
        } catch (Exception ex) {
            log.warn("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }

    /**
     * xml转换为map
     *
     * @param request
     * @return
     * @throws IOException
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) throws Exception {
        try {
            Map<String, String> data = new HashMap<>();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputStream stream = request.getInputStream();
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();

            for (int index = 0; index < nodeList.getLength(); ++index) {
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
            }
            return data;
        } catch (Exception ex) {
            log.warn("Invalid XML, can not convert to map. Error message: {}.", ex.getMessage());
            throw ex;
        }
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        org.w3c.dom.Document document = documentBuilder.newDocument();
        String tagName = "xml";
        Element root = document.createElement(tagName);
        document.appendChild(root);
        for (String key : data.keySet()) {
            String value = data.get(key);
            if (value == null)
                value = "";
            value = value.trim();
            Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString();
        try {
            writer.close();
        } catch (Exception ex) {
        }
        return output;
    }

    /**
     * java 转换成xml
     *
     * @param obj 对象实例
     * @return String xml字符串
     * @Title: toXml
     * @Description:
     */
    public static String toXml(Object obj) {
        XStream xstream = new XStream();
        ////如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        xstream.processAnnotations(obj.getClass()); //通过注解方式的，一定要有这句话
        return StringEscapeUtils.unescapeXml(xstream.toXML(obj));
    }

    /**
     * 将传入xml文本转换成Java对象
     *
     * @param xmlStr
     * @param cls    xml对应的class类
     * @return T   xml对应的class类的实例对象
     * <p>
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     * @Title: toBean
     * @Description:
     */
    public static <T> T toBean(String xmlStr, Class<T> cls) {
        //注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        T obj = (T) xstream.fromXML(xmlStr);
        return obj;
    }

    /**
     * 写到xml文件中去
     *
     * @param obj      对象
     * @param absPath  绝对路径
     * @param fileName 文件名
     * @return boolean
     * @Title: writeXMLFile
     * @Description:
     */

    public static boolean toXMLFile(Object obj, String absPath, String fileName) {
        String strXml = toXml(obj);
        String filePath = absPath + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                log.error("创建{" + filePath + "}文件失败!!!");
                return false;
            }
        }// end if
        OutputStream ous = null;
        try {
            ous = new FileOutputStream(file);
            ous.write(strXml.getBytes());
            ous.flush();
        } catch (Exception e1) {
            log.error("写{" + filePath + "}文件失败!!!");
            return false;
        } finally {
            if (ous != null)
                try {
                    ous.close();
                } catch (IOException e) {
                    log.error("写{" + filePath + "}文件关闭输出流异常!!!");
                }
        }
        return true;
    }
}
