package il.co.ilrd.observer;
class Maarive{
	Subject<String> maariv = new Subject<String>();
}
class Bibi{
	CallBack<String> bibi = new CallBack<String>(a->System.out.println(a), ()->System.out.println("bibi unsubscribed"));
}
class Gantz{
	CallBack<String> gantz = new CallBack<String>(a->System.out.println(a), ()->System.out.println("gantz unsubscribe"));
}

public class Test {

	public static void main(String[] args) {
		Maarive news = new Maarive();
		Bibi primeMinister = new Bibi();
		Gantz blueAndWhite = new Gantz();

		news.maariv.register(primeMinister.bibi);
		news.maariv.register(blueAndWhite.gantz);
		
		news.maariv.notifyAllObservers("breaking news");
		news.maariv.notifyAllObservers("miri regev is in blue and white");
		news.maariv.unregister(primeMinister.bibi);
		news.maariv.notifyAllObservers("you are fake news");
		
		
		
		news.maariv.register(primeMinister.bibi);
		news.maariv.notifyAllObservers("bibi you have to read my news");
		news.maariv.notifyAllObservers("bibi doesnt want to subscribe");
		news.maariv.notifyAllObservers("maarive is out of business");
		news.maariv.stop();
		news.maariv.notifyAllObservers("no one reads maariv");
		news.maariv.register(primeMinister.bibi);
		news.maariv.register(blueAndWhite.gantz);
		news.maariv.notifyAllObservers("we are back!");
		
		news.maariv.unregister(primeMinister.bibi);
		news.maariv.unregister(blueAndWhite.gantz);
		
		news.maariv.notifyAllObservers("all by myself");
	}
}
