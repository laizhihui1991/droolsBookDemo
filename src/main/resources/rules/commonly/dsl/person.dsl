[when]我的属性=$p:Person()
[when]只输出来家二少的属性=$p:Person(name=="来家二少")
[when]- 年龄 小于或等于 {年龄}=age<={年龄}
[when]- 名字 是 {名字}=name=={名字}
[when]是===
[when]equals===
[when]小于或等于=<=
[when]- 年龄=age
[when]- 名字=name
[when]{field:[0]} {operator} {value:[0]}={field} {operator} {value}
[then]打印出我的属性=System.out.println($p);
[then]打印出 {names:[\u4e00-\u9fa5_a-zA-Z0-9^]+}=System.out.println("{names}");
