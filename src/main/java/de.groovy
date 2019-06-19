import com.wch.test.Foo
import org.springframework.context.ApplicationContext
import org.springframework.context.support.ClassPathXmlApplicationContext


def context = new ClassPathXmlApplicationContext("applicationContext2.xml")
Foo foo = context.getBean(Foo.class);

inx = new Scanner(System.in);
while (true) {
    System.out.println("输入任意键");
    inx.next();
    foo.execute();
}

