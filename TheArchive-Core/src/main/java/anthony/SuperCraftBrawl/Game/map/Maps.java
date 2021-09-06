package anthony.SuperCraftBrawl.Game.map;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.Vector;

import anthony.SuperCraftBrawl.Game.GameType;

public enum Maps {

	NightDragon("NightDragon", new MapInstance("nightdragon")
			.setSpawnPos(new Vector(206.442, 146, 372.380), new Vector(217.503, 150, 376.405),
					new Vector(230.433, 147, 366.369), new Vector(230.479, 146, 389.485),
					new Vector(214.470, 149, 409.434), new Vector(213.426, 143, 392.416))
			.setLobbyLoc(new Vector(162.522, 151, 369.520)).setSpecLoc(new Vector(211.499, 152.19487, 389.566))
			.setBounds(new Vector(216.500, 160, 382.500), 35, 45)),

	Village("Village", new MapInstance("village")
			.setSpawnPos(new Vector(-1215.505, 127, 1041.604), new Vector(-1190.541, 128, 1028.559),
					new Vector(-1178.458, 128, 1000.517), new Vector(-1202.519, 129, 971.448),
					new Vector(-1179.535, 127, 969.981), new Vector(-1224.583, 129, 1006.430))
			.setLobbyLoc(new Vector(-1259.480, 130, 1008.520)).setSpecLoc(new Vector(-1194.847, 137.13280, 1011.121))
			.setBounds(new Vector(-1200.500, 142, 1003.500), 42, 50)),

	/*NorthernSeas("NorthernSeas",
			new MapInstance("northernseas")
					.setSpawnPos(new Vector(-1268.869, 165, 998.872), new Vector(-1273.602, 143, 984.472),
							new Vector(-1257.214, 117, 959.390), new Vector(-1241.536, 119, 941.453),
							new Vector(-1241.404, 158, 966.367), new Vector(-1222.527, 116, 962.654))
					.setLobbyLoc(new Vector(-1239.519, 125, 890.484)).setSpecLoc(new Vector(-1241.538, 162, 969.491))
					.setBounds(new Vector(-1241.500, 165, 979.500), 60, 60)),*/

	Candyland("Candyland",
			new MapInstance("candylandclassic")
					.setSpawnPos(new Vector(-1260.987, 133, 1001.699), new Vector(-1246.512, 142, 1011.427),
							new Vector(-1244.523, 142, 1018.460), new Vector(-1238.533, 169, 1015.462),
							new Vector(-1266.582, 144, 1023.429), new Vector(-1279.072, 145, 1007.997))
					.setLobbyLoc(new Vector(-1259.504, 136, 960.513)).setSpecLoc(new Vector(-1264.500, 157, 1011.500))
					.setBounds(new Vector(-1265.532, 160, 1010.443), 40, 32)),

	/*EggHunt("EggHunt", new MapInstance("egghunt")
			.setSpawnPos(new Vector(-1224.297, 153, 972.793), new Vector(-1239.013, 161, 991.791),
					new Vector(-1274.368, 147, 992.237), new Vector(-1276.799, 167, 962.813),
					new Vector(-1215.393, 142, 945.668), new Vector(-1265.496, 142, 926.329))
			.setLobbyLoc(new Vector(-1242.535, 151, 877.519)).setSpecLoc(new Vector(-1248.500, 168, 963.500))
			.setBounds(new Vector(-1239.549, 162, 960.644), 67, 65)),*/

	JungleRiver("JungleRiver", new MapInstance("jungleriver")
			.setSpawnPos(new Vector(-1286.582, 99, 963.440), new Vector(-1289.416, 111, 991.638),
					new Vector(-1255.538, 105, 998.475), new Vector(-1226.471, 121, 993.462),
					new Vector(-1229.484, 113, 957.462), new Vector(-1256.505, 134, 944.422))
			.setLobbyLoc(new Vector(-1328.460, 122, 972.520)).setSpecLoc(new Vector(-1241.064, 139.40382, 977.987))
			.setBounds(new Vector(-1249, 142, 976.499), 60, 65)),

	Stronghold("Stronghold", new MapInstance("stronghold")
			.setSpawnPos(new Vector(112.468, 152, -588.591), new Vector(99.516, 145, -586.557),
					new Vector(111.502, 150, -557.542), new Vector(96.431, 148, -551.543),
					new Vector(92.484, 151, -523.467), new Vector(81.435, 148, -539.514))
			.setLobbyLoc(new Vector(106.496, 152, -620.492)).setSpecLoc(new Vector(98.559, 155.27324, -561.960))
			.setBounds(new Vector(99.500, 163, -553.500), 35, 50)),

	Iceland("Iceland", new MapInstance("iceland")
			.setSpawnPos(new Vector(-617.635, 106, -10.515), new Vector(-599.636, 106, 4.459),
					new Vector(-554.533, 111, 25.511), new Vector(-578.518, 121, -38.583),
					new Vector(-585.487, 120, -21.634), new Vector(-559.512, 120, -31.692))
			.setLobbyLoc(new Vector(-579.478, 115, 63.434)).setSpecLoc(new Vector(-572.825, 127.77250, -10.992))
			.setBounds(new Vector(-585.519, 129, -10.543), 60, 55)),

	Marooned("Marooned",
			new MapInstance("marooned")
					.setSpawnPos(new Vector(41, 98, 2), new Vector(59, 103, 41), new Vector(78, 108, 18),
							new Vector(103, 98, 19), new Vector(93, 98, -7), new Vector(61, 106, -14))
					.setSpecLoc(new Vector(46.857, 108.11874, 7.306)).setLobbyLoc(new Vector(61.508, 115, -77.462))
					.setBounds(new Vector(70.500, 116, 7.500), 60, 60)),

	Orbital("Orbital", new MapInstance("orbital")
			.setSpawnPos(new Vector(50.3, 96, -18.5), new Vector(73.4, 100.5, -13.5), new Vector(73.4, 97, 11.4),
					new Vector(65.4, 114, 27.3), new Vector(51.5, 102, 27.4), new Vector(44.5, 97, 12.5))
			.setSpecLoc(new Vector(55.424, 113, 6.820)).setBounds(new Vector(55.484, 113, 6.503), 35, 40).setGameType(GameType.DUEL)),
	TheEnd("TheEnd", new MapInstance("theend")
			.setSpawnPos(new Vector(398.589, 162, 219.495), new Vector(365.481, 148, 234.525),
					new Vector(368.491, 147, 197.447), new Vector(348.422, 148, 215.429),
					new Vector(352.418, 163, 223.484), new Vector(391.487, 148, 209.432))
			.setLobbyLoc(new Vector(374.503, 151, 162.511)).setSpecLoc(new Vector(364.373, 161.05132, 215.139))
			.setBounds(new Vector(373.442, 159, 218.488), 50, 40)),

	Tropical("Tropical", new MapInstance("tropical")
			.setSpawnPos(new Vector(408.509, 120, 122.405), new Vector(436.509, 122.25000, 156.559),
					new Vector(422.545, 102, 173.471), new Vector(380.358, 103, 155.491),
					new Vector(404.051, 120, 157.006), new Vector(424.504, 103, 140.493))
			.setLobbyLoc(new Vector(333.583, 107, 154.515)).setSpecLoc(new Vector(403.845, 124.41554, 157.094))
			.setBounds(new Vector(404.461, 123, 152.472), 50, 50)),

	Gateway("Gateway",
			new MapInstance("gateway")
					.setSpawnPos(new Vector(372.481, 102, 168.417), new Vector(392.537, 102, 188.440),
							new Vector(372.445, 102, 208.485), new Vector(353.420, 102, 188.476),
							new Vector(345.435, 104, 211.468), new Vector(345.507, 106, 162.408))
					.setLobbyLoc(new Vector(369.458, 115, 258.441)).setBounds(new Vector(372.483, 128, 188.497), 50, 50)
					.setGameType(GameType.DUEL)),

	Pokemob("Pokemob",
			new MapInstance("pokemob")
					.setSpawnPos(new Vector(382.598, 109, 255.503), new Vector(372.513, 105, 237.486),
							new Vector(352.448, 110, 224.399), new Vector(321.346, 107, 254.454),
							new Vector(337.445, 105, 233.402), new Vector(373.408, 108, 275.570))
					.setLobbyLoc(new Vector(286.560, 113, 254.505)).setBounds(new Vector(353.450, 125, 254.414), 50, 50)),

	Revenge("Revenge",
			new MapInstance("atronach")
					.setSpawnPos(new Vector(47.3, 99, 1.4), new Vector(83.4, 99, -31.5), new Vector(120.4, 99, 3.4),
							new Vector(83.5, 99, 39.4), new Vector(87.4, 136, 5.4), new Vector(82.5, 103, 8.4))
					.setLobbyLoc(new Vector(-8.435, 116, -2.488)).setSpecLoc(new Vector(87.665, 146.01941, 6.043))
					.setBounds(new Vector(83.500, 150, 3.500), 60, 60).setGameType(GameType.FRENZY)),

	Atronach("Atronach",
			new MapInstance("atronach2")
					.setSpawnPos(new Vector(64.473, 107, -94.528), new Vector(87.499, 107, -70.563),
							new Vector(70.451, 107, -24.514), new Vector(41.474, 107, -22.505),
							new Vector(19.435, 107, -42.521), new Vector(19.404, 107, -67.537))
					.setLobbyLoc(new Vector(57.475, 109.31250, -130.494)).setSpecLoc(new Vector(59.525, 157, -64.436))
					.setBounds(new Vector(55.500, 174, -57.500), 58, 58).setGameType(GameType.FRENZY)),

	Elven("Elven",
			new MapInstance("elven").setSpawnPos(new Vector(8.498, 150, -50.562), new Vector(23.065, 150, -26.283),
					new Vector(45.160, 150, -6.369), new Vector(48.377, 150, 16.092), new Vector(29.859, 150, 29.376),
					new Vector(8.354, 150, 71.438), new Vector(-8.759, 150, 50.796), new Vector(-37.138, 150, 74.446),
					new Vector(-63.676, 150, 48.281), new Vector(-86.450, 150, 19.555),
					new Vector(-87.639, 150, -1.816), new Vector(-48.328, 150, -38.650),
					new Vector(-19.484, 173, 15.608), new Vector(-16.459, 173, 0.535), new Vector(-21.527, 233, -4.469),
					new Vector(-40.583, 218, -38.567)).setLobbyLoc(new Vector(-20.511, 165, -123.424))
					.setSpecLoc(new Vector(-22.500, 254, 6.500)).setBounds(new Vector(-22.500, 254, 1.500), 100, 100)
					.setGameType(GameType.FRENZY)),

	Limbo("Limbo", new MapInstance("limbo")
			.setSpawnPos(new Vector(-7.567, 152, -171.522), new Vector(22.390, 153, -191.667),
					new Vector(62.603, 165, -176.552), new Vector(65.449, 152, -177.584),
					new Vector(40.460, 187, -155.496), new Vector(13.575, 151, -129.550))
			.setLobbyLoc(new Vector(-58.467, 159, -142.483)).setSpecLoc(new Vector(39.103, 189.01158, -155.759))
			.setBounds(new Vector(24.500, 162, -155.500), 60, 60).setGameType(GameType.FRENZY)),

	Treehouse("Treehouse", new MapInstance("treehouse")
			.setSpawnPos(new Vector(-51.547, 110, 128.419), new Vector(-16.515, 110, 145.429),
					new Vector(-37.481, 110, 173.482), new Vector(-78.614, 110, 161.523),
					new Vector(-52.559, 138, 152.279), new Vector(-61.494, 160, 165.318))
			.setLobbyLoc(new Vector(-49.531, 140, 88.486)).setSpecLoc(new Vector(-53.866, 161.16555, 158.849))
			.setBounds(new Vector(-45.518, 173, 162.461), 60, 58).setGameType(GameType.FRENZY)),

	Frigid("Frigid",
			new MapInstance("frigid")
					.setSpawnPos(new Vector(-3.453, 103, 91.461), new Vector(-28.521, 99, 95.573),
							new Vector(-39.670, 104, 97.445), new Vector(-21.557, 141, 92.255),
							new Vector(5.463, 135, 62.412), new Vector(-81.633, 119, 92.505))
					.setLobbyLoc(new Vector(45.440, 136, 93.448)).setSpecLoc(new Vector(-18.295, 140, 92.952))
					.setBounds(new Vector(-43.545, 146, 92.435), 70, 70).setGameType(GameType.FRENZY)),

	DragonsDescent("DragonsDescent",
			new MapInstance("dragonsdescent")
					.setSpawnPos(new Vector(482.369, 156, -9.502), new Vector(488.629, 142, -27.326),
							new Vector(530.886, 136, -38.717), new Vector(528.271, 133, 3.666),
							new Vector(526.471, 133, 12.314), new Vector(530.452, 136, -24.538))
					.setLobbyLoc(new Vector(498.491, 151, -71.452)).setSpecLoc(new Vector(525.965, 152.19487, -24.033))
					.setBounds(new Vector(511.485, 154, -17.473), 55, 38)),

	LostTemple("LostTemple", new MapInstance("losttemple")
			.setSpawnPos(new Vector(1015.432, 174, -1082.684), new Vector(1057.729, 175, -1044.553),
					new Vector(1015.528, 176, -998.423), new Vector(972.433, 174, -1044.578),
					new Vector(1025.582, 183, -1040.508), new Vector(979.472, 172, -1080.703))
			.setLobbyLoc(new Vector(1017.507, 172, -1131.436)).setSpecLoc(new Vector(1015.468, 179.06868, -1044.676))
			.setBounds(new Vector(1015.469, 174, -1044.491), 60, 60).setGameType(GameType.FRENZY)),

	Multiverse("Multiverse", new MapInstance("multiverse")
			.setSpawnPos(new Vector(5027.470, 178, 697.573), new Vector(5048.384, 159.5, 712.458),
					new Vector(5045.360, 174, 739.441), new Vector(5092.187, 164, 731.663),
					new Vector(5075.565, 166, 764.524), new Vector(5069.549, 192, 734.550))
			.setLobbyLoc(new Vector(5042.516, 178, 657.538)).setSpecLoc(new Vector(5054.805, 187.05035, 716.117))
			.setBounds(new Vector(5059.500, 201, 732.500), 60, 58)),

	Apex("Apex", new MapInstance("apex")
			.setSpawnPos(new Vector(5025.599, 123, 657.440), new Vector(5065.488, 123, 663.495),
					new Vector(5056.512, 127, 680.502), new Vector(5033.467, 125, 693.585),
					new Vector(5009.634, 123, 680.307), new Vector(5029.894, 139, 694.579))
			.setLobbyLoc(new Vector(5035.477, 137, 597.452)).setSpecLoc(new Vector(5036.585, 140.16749, 668.634))
			.setBounds(new Vector(5038.500, 143, 676.500), 50, 45)),

	NetherFortress("NetherFortress", new MapInstance("netherfortress")
			.setSpawnPos(new Vector(5031.383, 145, 585.477), new Vector(5060.463, 150, 591.422),
					new Vector(5065.438, 158, 624.535), new Vector(5038.497, 145, 635.504),
					new Vector(5059.595, 145, 606.429), new Vector(5038.436, 138, 591.371))
			.setLobbyLoc(new Vector(5000.505, 155, 597.490)).setSpecLoc(new Vector(5050.741, 155.40139, 606.475))
			.setBounds(new Vector(5052.502, 160, 610.468), 30, 45)),

	Clockwork("Clockwork", new MapInstance("clockwork")
			.setSpawnPos(new Vector(5001.994, 174, 606.994), new Vector(5015.599, 158, 606.437),
					new Vector(5001.069, 134, 592.949), new Vector(5000.984, 159, 592.974),
					new Vector(4985.440, 151, 605.448), new Vector(5001.538, 152, 588.452))
			.setLobbyLoc(new Vector(4941.517, 162, 596.499)).setSpecLoc(new Vector(5001.898, 175.41397, 605.166))
			.setBounds(new Vector(4999.434, 178, 604.478), 35, 30)),

	PileOfBodies("PileOfBodies", new MapInstance("pileofbodies")
			.setSpawnPos(new Vector(4905.390, 141, 581.469), new Vector(4912.392, 149, 539.494),
					new Vector(4957.479, 145, 527.348), new Vector(4991.572, 145, 550.995),
					new Vector(4975.500, 150, 568.537), new Vector(4961.483, 146, 593.510))
			.setLobbyLoc(new Vector(4946.512, 153, 631.445)).setSpecLoc(new Vector(4946.051, 154.19487, 570.383))
			.setBounds(new Vector(4947.393, 162, 560.426), 60, 55)),

	Aperature("Aperature", new MapInstance("aperature")
			.setSpawnPos(new Vector(4962.509, 169, 630.467), new Vector(4950.443, 162, 633.424),
					new Vector(4929.372, 169, 633.473), new Vector(4929.443, 170, 670.440),
					new Vector(4963.443, 167, 670.540), new Vector(4945.540, 176, 656.476))
			.setLobbyLoc(new Vector(4950.502, 173, 587.524)).setSpecLoc(new Vector(4947.441, 180.73035, 651.318))
			.setBounds(new Vector(4947.520, 180, 651.493), 35, 35)),

	Mushroom("Mushroom", new MapInstance("mushroom")
			.setSpawnPos(new Vector(4983.498, 150, 806.436), new Vector(4989.533, 154, 831.486),
					new Vector(4970.462, 153, 840.350), new Vector(4959.440, 153, 858.484),
					new Vector(4983.457, 148, 887.480), new Vector(4985.492, 153, 856.439))
			.setLobbyLoc(new Vector(4906.508, 156, 853.509)).setSpecLoc(new Vector(4977.357, 159.40111, 850.104))
			.setBounds(new Vector(4972.566, 160, 847.504), 35, 60)),
	MushroomCastle("MushroomCastle",
			new MapInstance("mushroomcastle")
					.setSpawnPos(new Vector(4909.508, 148, 861.517), new Vector(4932.512, 148, 876.490),
							new Vector(4909.417, 148, 892.463))
					.setLobbyLoc(new Vector(4907.468, 150, 944.508)).setSpecLoc(new Vector(4909.448, 195, 876.408))
					.setBounds(new Vector(4909.448, 195, 876.408), 40, 40).setGameType(GameType.DUEL)),
	
	
	Archfield("Archfield", new MapInstance("archfield")
			.setSpawnPos(new Vector(4918.457, 146, 773.510), new Vector(4928.979, 145, 778.960),
					new Vector(4938.442, 144, 781.423), new Vector(4934.485, 154, 797.487),
					new Vector(4939.498, 148, 794.459))
			.setLobbyLoc(new Vector(4930.479, 144, 738.447)).setSpecLoc(new Vector(4928.500, 161, 784.500))
			.setBounds(new Vector(4928.470, 161, 788.485), 32, 33)),
	
	NetherCastle("NetherCastle", new MapInstance("nethercastle")
			.setSpawnPos(new Vector(4936.450, 148, 738.493), new Vector(4929.438, 154, 715.461),
					new Vector(4956.483, 148, 718.454), new Vector(4976.488, 148, 738.484),
					new Vector(4956.460, 155, 729.497))
			.setLobbyLoc(new Vector(4884.519, 146, 733.478)).setSpecLoc(new Vector(4956.479, 149, 738.454))
			.setBounds(new Vector(4956.479, 149, 738.454), 50, 50)),
	
	CherryGrove("CherryGrove", new MapInstance("cherrygrove")
			.setSpawnPos(new Vector(4944.457, 153, 738.447), new Vector(4957.483, 153, 738.452),
					new Vector(5001.518, 166, 787.520), new Vector(4956.500, 165, 722.401),
					new Vector(4935.409, 160, 739.443))
			.setLobbyLoc(new Vector(4894.390, 158, 748.594)).setSpecLoc(new Vector(4951.397, 154, 738.449))
			.setBounds(new Vector(4969.568, 183, 738.493), 50, 70)),
	
	Mountain("Mountain", new MapInstance("mountain")
			.setSpawnPos(new Vector(4885.463, 152, 790.549), new Vector(4864.387, 152, 790.485),
					new Vector(4867.388, 168, 781.455), new Vector(4904.560, 187, 788.443),
					new Vector(4874.485, 160, 800.518))
			.setLobbyLoc(new Vector(4819.523, 186, 788.498)).setSpecLoc(new Vector(4885.436, 158, 790.454))
			.setBounds(new Vector(4885.436, 158, 790.454), 45, 40)),
	
	Mansion("Mansion", new MapInstance("mansion")
			.setSpawnPos(new Vector(4920.444, 152, 776.414), new Vector(4953.547, 152, 793.375),
					new Vector(4954.586, 145, 838.512), new Vector(4925.501, 149, 815.555),
					new Vector(4911.503, 157, 814.491))
			.setLobbyLoc(new Vector(4933.467, 151, 719.522)).setSpecLoc(new Vector(4922.493, 149, 812.282))
			.setBounds(new Vector(4932.483, 188, 811.481), 50, 60)),
	
	TempleOfMars("TempleOfMars", new MapInstance("templeofmars")
			.setSpawnPos(new Vector(4962.489, 155, 742.402), new Vector(4946.502, 153, 788.490),
					new Vector(4918.471, 149, 768.472), new Vector(4888.509, 148, 779.695),
					new Vector(4888.461, 158, 782.541))
			.setLobbyLoc(new Vector(4930.449, 163, 690.517)).setSpecLoc(new Vector(4930.428, 160, 772.552))
			.setBounds(new Vector(4930.428, 160, 772.552), 60, 40)),
	
	
	YingYang("YingYang", new MapInstance("yingyang")
			.setSpawnPos(new Vector(4947.503, 143, 853.543), new Vector(4917.487, 142, 878.623),
					new Vector(4889.500, 143, 860.500), new Vector(4892.485, 145, 841.489),
					new Vector(4915.427, 141, 853.582))
			.setLobbyLoc(new Vector(4917.443, 151, 788.557)).setSpecLoc(new Vector(4916.451, 184, 853.452))
			.setBounds(new Vector(4916.449, 184, 853.503), 47, 47))
	/*
	 * Oriental("Oriental", new MapInstance("Oriental") .setSpawnPos(new
	 * Vector(4961.474, 154, 735.429), new Vector(4961.455, 162, 737.563), new
	 * Vector(4961.532, 169, 884.514), new Vector(4971.451, 191, 800.483), new
	 * Vector(4939.542, 166, 802.498)) .setLobbyLoc(new Vector(4962.484, 169,
	 * 689.498)).setSpecLoc(new Vector(4960.544, 182, 765.681)) .setBounds(new
	 * Vector(, 184, 853.503), 47, 47))
	 */;

	private final MapInstance instance;
	private final String name;

	Maps(String name, MapInstance instance) {
		this.instance = instance;
		this.name = name;
	}

	public MapInstance GetInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public static List<Maps> getGameType(GameType type) {
		List<Maps> maps = new ArrayList<>();

		for (Maps map : Maps.values()) {
			if (map.GetInstance().gameType == type) {
				maps.add(map);
			}
		}
		return maps;
	}
}
