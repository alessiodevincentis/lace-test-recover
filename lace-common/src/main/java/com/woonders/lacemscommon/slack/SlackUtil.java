package com.woonders.lacemscommon.slack;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.webhook.Payload;

/**
 * Created by Emanuele on 15/02/2017.
 */
@Component
public class SlackUtil {

	private static final String WOONDERS_SLACK_LACEMS_WEBHOOKS = "https://hooks.slack.com/services/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";

	public void sendMessage(String message, SlackEmojiIcon slackEmojiIcon, Exception exception, Throwable throwable) {
		if (exception != null) {
			message = StringUtils.join(Arrays.asList(message, exception.toString()), " ");
		}

		if (throwable != null) {
			message = StringUtils.join(Arrays.asList(message, throwable.toString()), " ");
		}

		Payload.PayloadBuilder payloadBuilder = Payload.builder().text(message);
		if (slackEmojiIcon != null) {
			payloadBuilder.iconEmoji(slackEmojiIcon.getValue());
		}

		Slack slack = Slack.getInstance();
		try {
			slack.send(WOONDERS_SLACK_LACEMS_WEBHOOKS, payloadBuilder.build());
		} catch (IOException e) {
			// errore di comunicazione per inviare il messaggio su slack
		}
	}

	public void sendMessage(String message, SlackEmojiIcon slackEmojiIcon, Exception exception) {
		sendMessage(message, slackEmojiIcon, exception, null);
	}

	public void sendMessage(String message, SlackEmojiIcon slackEmojiIcon, Throwable throwable) {
		sendMessage(message, slackEmojiIcon, null, throwable);
	}

	public void sendMessage(String message, SlackEmojiIcon slackEmojiIcon) {
		sendMessage(message, slackEmojiIcon, null);
	}

	public void sendMessage(String message) {
		sendMessage(message, null, null);
	}

	public enum SlackEmojiIcon {
		CASH(":moneybag:"), BOMB(":bomb:");

		private String value;

		SlackEmojiIcon(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
