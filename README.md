# PSD2
Berlin Group XS2A Implementation for the OAuth2 approach

In this github archive you will find our view of the Berlin Group Interpretation of PSD2 XS2A.

As strong believers an open standards, there for we choose the XAuth2 approach.

You will find in the folder xs2a our ASPSP (Account Servicing Payment Service Provider) implementation 
without any connection to our core banking system. In fact the implementation is runnable on Java EE 7 
stacks (we develop on Wildfly and Websphere Liberty Profile) und we connected a mock core banking system.

Under the tpp folder we provide a showcase implementation. Our main focus is to give a better understanding 
on the workflows and how this API should be used.  Therefore it’s technical focused. For how to use, please view our clip on youtube.

By using OAuth an identity provider is mandatory. To setup a local Identity Provider is not an easy 
job so we decided to provide a simple mock implementation. This mock is (as our backend implementation) runnable on a standard Java EE 7 stack.

Security aspects like check correctness of an access token is not part of this code. We are solving this kind of issues in 
components like firewalls, API-gateways, … like most of our competitor.
