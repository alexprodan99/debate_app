# debate_app
Debate app using java sockets for token passing and pub sub (rabbitmq) for message transmission and reception.Each peer has a timeout(30 seconds) to transmit messages on topics that he can "publish" to and also he is "subscriber" on same topics and he can receive messages related to topics he is subscribed to.
Debaters are arranged in a circle and each debater can publish only if he's having token. Token is passed among the debaters.If an debater is publishing an message, token is passed to neighbor debater. Last debater is passing to the first debater (cyclic structure).Also token is passed if 30 seconds passed and debater with token is not transmitting any message.
