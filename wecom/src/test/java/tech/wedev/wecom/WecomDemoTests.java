package tech.wedev.wecom;

import lombok.var;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tech.wedev.wecom.api.entity.BaseFunctional;
import tech.wedev.wecom.entity.po.WecomMarketArticlePO;
import tech.wedev.wecom.enums.AttachmentsMsgTypeEnum;
import tech.wedev.wecom.monad.Try;
import tech.wedev.wecom.request.RequestV1Private;
import tech.wedev.wecom.utils.ObjectUtils;
import tech.wedev.wecom.utils.SM4Util;
import tech.wedev.wecom.utils.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringRunner.class)
public class WecomDemoTests {

	@Value("${spring.datasource.password}")
	private String datasource_password;

	@Value("${spring.redis.password}")
	private String redis_password;

	@Test
	public void test3() {
		Map<String, String> map0 = new TreeMap<>();
		map0.put("a", "I'am a");
		map0.put("c", "I'am c");
		map0.put("d", "I'am d");
		map0.put("b", "I'am b");
		/*for (Map.Entry<String, String> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}*/
		map0.forEach((a,b)->{
			//System.out.println(a+ "我是空格" +b);
		});

		/*Map排序方法二：使用stream进行排序*/
		Map<Integer, String> map1 = new HashMap<>();
		map1.put(100, "I'am a");
		map1.put(99, "I'am c");
		map1.put(2, "I'am d");
		map1.put(33, "I'am b");
		// 按照value进行倒排，如果要根据key进行排序的话使用Map.Entry.comparingByKey()
		map1.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).forEach(System.out::println);
		// 根据value进行正序排序，根据key进行排序的话使用comparingByKey()
		map1.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(System.out::println);

		// 如果要将排序的结果返回的话，我们可以使用下面的方法（注意：要使用LinkedHashMap进行保存，linkedHashMap可以保存插入顺序）
		Map<Integer, String> resultMap1 = map1.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, s2) -> s,
				LinkedHashMap::new));
		resultMap1.forEach((a,b)->{
			System.out.println(a+ "我是空格-resultMap1" +b);
		});
	}

	@Test
	public void test5() {
		System.out.println(ObjectUtils.strToType("100", Integer.class) + 1);
		ListUtils.partition(new ArrayList<>(), 200).stream().filter(CollectionUtils::isNotEmpty).forEach(System.out::println);
	}

	@Test
	public void test6() throws Throwable {
		String msgType = "link";
		WecomMarketArticlePO article = new WecomMarketArticlePO();
		article.setArticleTitle("我是标题");
		article.setArticleAbstract("我是摘要");
		article.setArticleLink("我是链接");
		String picurl = "我是图片链接";
		String pic_media_id = "我是图片ID";
		Arrays.stream(AttachmentsMsgTypeEnum.values()).forEach(a -> a.setValue(picurl, pic_media_id));
		var attachments = Stream.of(new BaseFunctional<>(msgType)).map(m -> m.map(t -> {
			try {
				return Try.ofFailable(()-> BaseFunctional.transform(RequestV1Private.class, article, t)).get();
			} catch (Throwable e) {
				throw new RuntimeException(e);
			}
		})).findFirst().get().data;
		System.out.println(attachments);
	}

	@Test
	public void test7() {
		HashMap<String,Object> map = new HashMap<>();
		map.put("id_", "123");
		System.out.println(Optional.ofNullable(MapUtils.getString(map, "id")).orElse("空格"));
	}

	@Test
	public void test8() throws Exception {
		WecomMarketArticlePO article = new WecomMarketArticlePO();
		article.setArticleTitle("我是标题");
		article.setArticleAbstract("我是摘要");
		article.setArticleLink("我是链接");
		var name = RequestV1Private.Attachments.class.getName();
		var clazz = Class.forName("tech.wedev.wecom.request.RequestV1Private");
//		var getDeclaredFields0 = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
//		getDeclaredFields0.setAccessible(true);
//		var invoke = getDeclaredFields0.invoke(Class.class, false);
		var obj = ObjectUtils.strObjToType(Class.forName("tech.wedev.wecom.request.RequestV1Private$" + StringUtils.capitalizeFirstLetter("link")).newInstance(), RequestV1Private.Link.class);

		var declaredFields1 = Class.forName("com.stream.streamdemo.request.RequestV1Private$" + StringUtils.capitalizeFirstLetter("link")).getDeclaredFields();
		/*for (Field field : declaredFields1) {
			field.setAccessible(true);
			var map = Arrays.stream(AttachmentsMsgTypeEnum.values()).filter(a -> a.getDesc().equals("link"))
					.map(AttachmentsMsgTypeEnum::getMap)
					.findFirst()
					.orElseThrow(Exception::new);
			if (!Optional.ofNullable(map.get(field.getName())).isPresent()) {continue;}
			Field field1 = article.getClass().getDeclaredField(map.get(field.getName()));//articleTitle
			field1.setAccessible(true);
			field.set(obj,field1.get(article));//我是标题
		}*/
		var declaredFields = clazz.getDeclaredFields();
		var declaredClasses = clazz.getDeclaredClasses();
		Arrays.stream(declaredClasses)
				.filter(a -> a == RequestV1Private.Attachments.class)
				.findFirst()
				.ifPresent(b-> {
					try {
						var f1 = b.getDeclaredField("link");
						f1.setAccessible(true);
						var instance = b.newInstance();
						f1.set(ObjectUtils.strObjToType(instance, RequestV1Private.Attachments.class),obj);
						System.out.println(instance);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				});

		var instance = clazz.newInstance();
		var attachments = RequestV1Private.Attachments.class.newInstance();
		var fields = RequestV1Private.Attachments.class.getDeclaredFields();
		/*Arrays.stream(declaredClasses).forEach(a->{
			Arrays.stream(a.getDeclaredFields()).forEach(b-> Try.ofFailable(() -> {
						b.setAccessible(true);
						var obj = b.get(a);
						System.out.println("获取到的field, name = " + b.getName() + ",value=" + obj.toString());
						return null;
					}
				));
			System.out.println(a);
		});*/
	}

	@Test
	public void test9() throws Exception {
		var t1 = new Thread(() -> {
			try {
				Thread.sleep(10 * 1000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});

		synchronized(this){
			System.out.println(Thread.currentThread().getName()+" 开始执行休眠");
			Thread.sleep(10 * 1000);
			System.out.println(Thread.currentThread().getName()+" 休眠结束");
		}
	}

	@Test
	public void test10() {
		String replace = "123a-11".replaceAll("(.*\\-)", "");//11
		String className = "123$888";
		className = className.indexOf("$") == -1 ? className : StringUtils.subString(className, 0, className.indexOf("$"));//123
		className = !className.contains("$") ? className : StringUtils.subString(className, 0, className.indexOf("$"));//123
		System.out.println("replace: " + replace + ", className:" + className);
	}

	@Test
	public void test11() {
		System.out.println(SM4Util.encryptEcb(SM4Util.hexKey, "123456"));
//		System.out.println(SM4Util.decryptEcb(SM4Util.hexKey, datasource_password));
//		System.out.println(SM4Util.decryptEcb(SM4Util.hexKey, redis_password));
	}

	@Test
	public void test12() {
		String ip = "fe80:0:0:0:4f11:1554:4711:419%utun4";
		int hash = ip.hashCode();
		int machineId = 255 & ((hash ^ (hash >>> 16)) & 0x8fffffff);

		machineId = (machineId > 31 || machineId < 0) ? 1 : machineId;
		System.out.println(String.format("The machine Id is %d", machineId));
	}

}
