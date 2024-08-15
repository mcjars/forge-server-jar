package dev.mcvapi.forgeserverjar.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ServerBootstrap {
	private static class ProcessHolder {
		Process process;
		BufferedWriter writer;
		BufferedReader stdoutReader;
		BufferedReader stderrReader;
	}

	public void startServer(String[] cmd) throws ServerStartupException {
		ProcessHolder processHolder = new ProcessHolder();
		try {
			ProcessBuilder processBuilder = new ProcessBuilder(cmd);
			processHolder.process = processBuilder.start();

			processHolder.writer = new BufferedWriter(new OutputStreamWriter(processHolder.process.getOutputStream()));
			processHolder.stdoutReader = new BufferedReader(new InputStreamReader(processHolder.process.getInputStream()));
			processHolder.stderrReader = new BufferedReader(new InputStreamReader(processHolder.process.getErrorStream()));

			new Thread(() -> {
				try {
					String line;
					while ((line = processHolder.stdoutReader.readLine()) != null) {
						System.out.println(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();

			new Thread(() -> {
				try {
					String line;
					while ((line = processHolder.stderrReader.readLine()) != null) {
						System.err.println(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();

			new Thread(() -> {
				try (BufferedReader userInputReader = new BufferedReader(new InputStreamReader(System.in))) {
					String userInput;
					while ((userInput = userInputReader.readLine()) != null) {
						processHolder.writer.write(userInput + "\n");
						processHolder.writer.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}).start();

			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				if (processHolder.process != null && processHolder.process.isAlive()) {
					try {
						processHolder.writer.write("stop\n");
						processHolder.writer.flush();

						processHolder.process.waitFor();
					} catch (IOException | InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}));

			try {
				processHolder.process.waitFor();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		} catch (IOException exception) {
				throw new ServerStartupException("Failed to start the Forge server.", exception);
		} finally {
			if (processHolder.process != null && processHolder.process.isAlive()) {
				try {
					processHolder.writer.write("stop\n");
					processHolder.writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					processHolder.process.destroy();
					try {
						processHolder.process.waitFor();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		}
	}

	@SuppressWarnings("InnerClassMayBeStatic")
	public static class ServerStartupException extends Exception {
		ServerStartupException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
