# Simple AI module for demonstration
def analyze_data(data):
    # For example, count items or detect status
    summary = {"total": len(data), "running": 0, "stopped": 0, "enabled": 0, "disabled": 0}
    for item in data:
        status = item.get("status", "").lower()
        if status == "running":
            summary["running"] += 1
        elif status == "stopped":
            summary["stopped"] += 1
        elif status == "enabled":
            summary["enabled"] += 1
        elif status == "disabled":
            summary["disabled"] += 1
    return summary
