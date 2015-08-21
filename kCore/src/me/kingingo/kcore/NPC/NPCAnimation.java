package me.kingingo.kcore.NPC;
public enum NPCAnimation {

		SWING_ARM(
			0),
		TAKE_DAMAGE(
			1),
		LEAVE_BED(
			2),
		EAT_FOOD(
			3),
		CRITICAL_EFFECT(
			4),
		MAGIC_CRITICAL_EFFECT(
			5),
		CROUCH(
			104),
		UNCROUCH(
			105);

	private int	id;

	private NPCAnimation(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}
	
	public static NPCAnimation getId(int id){
		for(NPCAnimation a : NPCAnimation.values()){
			if(a.getId()==id){
				return a;
			}
		}
		return null;
	}
}