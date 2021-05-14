package org.miraiboot.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class Command {
	private String name;
	private ArrayList<String> args;

	public Command() {
		this.name = "";
		this.args = new ArrayList<String>();
	}

	public Command(String name, String... args) {
		this();
		this.name = name;
		this.args.addAll(Arrays.asList(args));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getArgs() {
		return args;
	}

	public Command setArgs(ArrayList<String> args) {
		this.args = args;
		return this;
	}

	public Command resetArgs() {
		this.args.clear();
		return this;
	}

	public Command addArgs(String arg) {
		this.args.add(arg);
		return this;
	}

	public Command resetAndAddArgs(String arg) {
		return this.resetArgs().addArgs(arg);
	}


	@Override
	public String toString() {
		return this.name + ", args: " + this.args.toString();
	}
}
