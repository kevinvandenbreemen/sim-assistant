import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'channels.dart';

const String ACTIONS = "actions";

const TextStyle defaultTextStyle = TextStyle(
    color: Colors.white,
    fontWeight: FontWeight.bold
);

void main() {
  runApp(new MaterialApp(
    home: new HelpScreenWidget(),
  ));
}

class HelpScreenWidget extends StatefulWidget {
  @override
  _State createState() => new _State();
}

class _State extends State<HelpScreenWidget> {

  String _version = "";

  BasicMessageChannel<String> _actionsChannel = new BasicMessageChannel(
      ACTIONS, StringCodec());

  @override
  Widget build(BuildContext context) {
    return new Container(
      padding: new EdgeInsets.all(32.0),
      child: new Column(
        children: <Widget>[
          new Text('Simming Catalogue and Dictator', style: TextStyle(
              color: Colors.lightGreen,
              fontWeight: FontWeight.bold
          ),),
          new Card(
            color: Colors.black54,
            child: new Column(
              children: <Widget>[
                new Text("By Kevin VanDenBreemen",
                  style: defaultTextStyle,
                ),
                new Text("V.  ${_version}", style: defaultTextStyle,),
                Divider(height: 1.0, color: Colors.white,),
                new Text(
                  "The handy dandy app for listening to the posts in your favourite sim while you drive or do chores.",
                  style: defaultTextStyle,
                )
              ],
            ),
          ),
          new FlatButton(
              color: Colors.white,
              textColor: Colors.black,
              onPressed: doClose, child: new Text("OK"))
        ],
      ),
    );
  }

  void doClose() {
    _actionsChannel.send(RETURN_TO_MAIN);
  }
}