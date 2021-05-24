package net.diyigemt.miraiboot;

import org.junit.jupiter.api.Test;

public class TestClass {
	class Base {
		public void say() {
			System.out.println("aaa");
		}
	}
	class A extends Base {
		private int c = 1;
		private void a() {
			System.out.println("bbb");
		}

		@Override
		public void say() {
			System.out.println("bbb" + c);
		}
	}
	@Test
	public void test() {
		test2(new A());
	}

	public <T extends Base> void test2(T clazz) {
		clazz.say();
	}
}
