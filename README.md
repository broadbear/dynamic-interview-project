
Dynamic Interview Project, 2024
Michael Broadbear

Link to app
https://peaceful-reaches-50301-4559649b3f6c.herokuapp.com/signup

Known issues
- The hosted app currently runs out of memory during encyrption/decryption when sending a transaction. It works fine on my laptop. I would rather not upgrade to a more powerful host server and am able to demonstrate it on my laptop.

Requirements
- Authenticated is able to create multiple wallets.
- All interactions with custodial wallets are on the backend via an API.
- The user can perform:
  1. get wallet balance (as seen on the dashboard)
  2. sign message (included in creating and sending a transfer transaction)
  3. send a transaction on the blockchain
- A basic UI exercises this functionality

Design decisions
- My most familiar language is Java so I chose to use the Spring Boot framework to implement the application.
- Web3 library: web3j, a Java-based library for interactoins with the blockchain.
- Chain: Etherium (Sepolia)
- Backend data store: No-SQL hosted MongoDB instance to store wallet files and some basic user information. This was determined adequate as storing wallet files as documents in MondoDB seemed like a good fit, and most lookups can be done by unique identifier with less need for relationships among DB entities.
- The application is deployed on heroku.
- The basic UI uses Bootstrap for styling.

Security considerations
- The application uses the Dynamic API to create users and provide email-based OTP access.
- The application relies on storing a value in the session in order to signify that a user is logged in. Improvements would be to enable this to expire, and/or to use Dynamic's ability to generate JWT authorization tokens which would enable authorization/authentication to be session-less. User session was chosen in the interest of saving time.
- Theoretically, no passwords are stored in the backend, only a hash, which reduces the chance that passwords can be hacked. Improvements here would be to add salt to avoid rainbow table lookups.
- Other improvements would be to limit the number of wallets a user can create and to add OTP authorization around more sensitive operations such as transferring eth.

